import 'package:demo/model/index.dart';
import 'package:demo/widgets/clip_tab/index.dart';
import 'package:flutter/material.dart';

class ClipTabController with ClipSlideStatusListenerMixin {
  static const double percentPerMillisecond = 0.001;

  ClipTabController({
    required TickerProvider vsync,
    length,
    int initialIndex = 0,
    this.slideSuccessProportion = .5,
  })
      : _index = initialIndex,
        _nextPageIndex = initialIndex,
        _length = length,
        _animationController = AnimationController(vsync: vsync);

  ///判定拖动成功的比例
  final double slideSuccessProportion;
  final AnimationController _animationController;
  final int _length;
  int _index;
  int _nextPageIndex;
  SlideDirection _slideDirection = SlideDirection.none;

  ///拖动开始的坐标
  Offset dragStartOffset = Offset.zero;

  ///拖动是否判定成功,如果拖动比例大于slideSuccessProportion为true,否则为false
  bool _isSlideSuccess = false;

  AnimationController get animationController => _animationController;

  int get index => _index;

  double get value => _animationController.value;

  int get nextPageIndex => _nextPageIndex;

  int get length => _length;

  Animation<double> get animation => _animationController.view;

  set index(int newIndex) {
    _index = newIndex;
    _nextPageIndex = index;
    value = 0;
  }

  int get nextPage => index + 1 >= length ? 0 : index + 1;

  int get previousPage => index - 1 < 0 ? length - 1 : index - 1;

  set value(double newValue) {
    _animationController.value = newValue;
  }

  addListener(VoidCallback listener) {
    _animationController.addListener(listener);
  }

  animationCompleted() {
    onSlideUpdate(SlideUpdate(slideStatus: SlideStatus.doneAnimated));
  }

  void animateTo(int nextPage,
      {Curve curve = Curves.ease, Duration duration = kTabScrollDuration}) {
    assert(nextPage >= 0 && (nextPage < length || length == 0));
    if (nextPage == _index || length < 2) return;
    _nextPageIndex = nextPage;
    _animationController.duration = duration;
    _animationController.forward(from: value).whenComplete(() =>
        animationCompleted());
  }

  toPreviousPage() {
    _isSlideSuccess = true;
    animateTo(previousPage);
  }

  toNextPage() {
    _isSlideSuccess = true;
    animateTo(nextPage);
  }

  onSlideUpdate(SlideUpdate slideUpdate) {
    notifyStatusListeners(slideUpdate.slideStatus);
    switch (slideUpdate.slideStatus) {
      case SlideStatus.dragStart:
        onDragStart(slideUpdate);
        break;
      case SlideStatus.dragging:
        onDragging(slideUpdate);
        break;
      case SlideStatus.doneDrag:
        onDragDone(slideUpdate);
        break;
      case SlideStatus.animating:
        onAnimating(slideUpdate);
        break;
      case SlideStatus.doneAnimated:
        onAnimatedDone();
        break;
    }
  }

  ///开始拖动
  onDragStart(SlideUpdate slideUpdate) {
    if(slideUpdate.dragStart != null){
      dragStartOffset = slideUpdate.dragStart!;
    }
  }

  ///拖动中
  onDragging(SlideUpdate slideUpdate) {
    if(slideUpdate.direction != null) {
      _slideDirection = slideUpdate.direction!;
    }
    if (_slideDirection == SlideDirection.leftToRight) {
      _nextPageIndex = previousPage;
    } else if (_slideDirection == SlideDirection.rightToLeft) {
      _nextPageIndex = nextPage;
    } else {
      _nextPageIndex = index;
    }
    if(slideUpdate.slidePercent != null) {
      value = slideUpdate.slidePercent!;
    }
  }

  ///拖动结束,开始动画.两种情况,根据滑动比例判断 < 0.5 -切换到下一页, >=0.5-回退到当前页.
  onDragDone(SlideUpdate slideUpdate) {
    onAnimatedStart(slideUpdate: slideUpdate);
  }

  ///动画开始
  onAnimatedStart({required SlideUpdate slideUpdate}) {
    Duration duration;
    _isSlideSuccess = value >= slideSuccessProportion;
    if (_isSlideSuccess) {
      final slideRemaining = 1.0 - value;
      duration = Duration(
          milliseconds: (slideRemaining / percentPerMillisecond).round());
      _animationController.duration = duration;
      _animationController.forward(from: value).whenComplete(() =>
          animationCompleted());
    } else {
      duration =
          Duration(milliseconds: (value / percentPerMillisecond).round());
      _animationController.duration = duration;
      _animationController.reverse(from: value);
    }
  }

  ///动画运行中
  onAnimating(SlideUpdate slideUpdate) {}

  ///动画结束
  onAnimatedDone() {
    if (_isSlideSuccess) {
      index = nextPageIndex;
    }
    _slideDirection = SlideDirection.none;
    value = 0.0;
  }

  void dispose() {
    _animationController.dispose();
  }
}

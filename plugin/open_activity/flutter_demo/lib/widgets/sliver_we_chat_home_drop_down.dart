import 'package:demo/util/assets_util.dart';
import 'package:demo/util/index.dart';
import 'package:demo/widgets/index.dart';
import 'package:flutter/material.dart';

class SliverWeChatHomeDropDown extends StatefulWidget {
  final Widget? child;

  ///页面的高度
  final double layoutExtent;

  ///底部剩余的高度
  final double bottomExtent;
  final ValueNotifier<bool> focusNotifier;

  const SliverWeChatHomeDropDown({
    Key? key,
    this.child,
    required this.layoutExtent,
    this.bottomExtent = 80.0,
    required this.focusNotifier,
  }) : super(key: key);

  @override
  _SliverWeChatHomeDropDownState createState() =>
      _SliverWeChatHomeDropDownState();
}

class _SliverWeChatHomeDropDownState extends State<SliverWeChatHomeDropDown> {
  bool hasLayoutExtent = false;
  double dropDownBoxExtent = 0.0;

  bool get focus => widget.focusNotifier.value;

  @override
  Widget build(BuildContext context) {
    return WeChatDropDown(
      hasLayoutExtent: hasLayoutExtent,
      layoutExtent: widget.layoutExtent,
      bottomExtent: widget.bottomExtent,
      child: LayoutBuilder(
        builder: (BuildContext context, BoxConstraints constraints) {
          dropDownBoxExtent = constraints.maxHeight;
          if (focus == false &&
              hasLayoutExtent == false &&
              dropDownBoxExtent >= 100) {
            WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
              setState(() {
                hasLayoutExtent = true;
              });
            });
            /*Future.delayed(Duration(milliseconds: 3000), () {
                setState(() {
                  hasLayoutExtent = false;
                });
              });*/
          }
          return _DefaultDropDownPage(
            dropDownBoxExtent: dropDownBoxExtent,
            layoutExtent: widget.layoutExtent,
          );
        },
      ),
    );
  }
}

class _DefaultDropDownPage extends StatefulWidget {
  final double dropDownBoxExtent;
  final double layoutExtent;

  const _DefaultDropDownPage({required this.dropDownBoxExtent, required this.layoutExtent});

  @override
  _DefaultDropDownPageState createState() => _DefaultDropDownPageState();
}

class _DefaultDropDownPageState extends State<_DefaultDropDownPage> {
  double get dropDownBoxExtent => widget.dropDownBoxExtent;

  double get layoutExtent => widget.layoutExtent;

  double get opacity {
    if (dropDownBoxExtent < 100) {
      return 1;
    }

    return (1 - dropDownBoxExtent / layoutExtent).clamp(0, 1).toDouble();
  }

  @override
  Widget build(BuildContext context) {
    return Stack(
      children: [
        Container(
          color: Colors.grey[300]?.withOpacity(.5),
        ),
        Positioned(
          top: 0,
          left: 0,
          right: 0,
          bottom: null,
          child: Container(
            color: Colors.grey[300],
            child: Image.asset(Assets.rem),
            foregroundDecoration:
                BoxDecoration(color: Colors.grey[300]?.withOpacity(opacity)),
          ),
        ),
      ],
    );
  }
}

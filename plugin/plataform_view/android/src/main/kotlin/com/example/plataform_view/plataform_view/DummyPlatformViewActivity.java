// Copyright 2014 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package com.example.plataform_view.plataform_view;

import androidx.annotation.NonNull;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;

public class DummyPlatformViewActivity extends FlutterActivity {
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        flutterEngine
            .getPlatformViewsController()
            .getRegistry()
            .registerViewFactory("benchmarks/platform_views_layout_hybrid_composition/DummyPlatformView", new DummyPlatformViewFactory());
    }
}

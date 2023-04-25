#import "PlataformViewPlugin.h"
#if __has_include(<plataform_view/plataform_view-Swift.h>)
#import <plataform_view/plataform_view-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "plataform_view-Swift.h"
#endif

@implementation PlataformViewPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftPlataformViewPlugin registerWithRegistrar:registrar];
}
@end

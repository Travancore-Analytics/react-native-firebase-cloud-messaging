//  Created by react-native-create-bridge

#import <UIKit/UIKit.h>
#import <UIKit/UIApplication.h>
#import "FirebasePushNotification.h"
#import <Firebase/Firebase.h>
// import RCTBridge
#if __has_include(<React/RCTBridge.h>)
#import <React/RCTBridge.h>
#elif __has_include(“RCTBridge.h”)
#import “RCTBridge.h”
#else
#import “React/RCTBridge.h” // Required when used as a Pod in a Swift project
#endif

// import RCTEventDispatcher
#if __has_include(<React/RCTEventDispatcher.h>)
#import <React/RCTEventDispatcher.h>
#elif __has_include(“RCTEventDispatcher.h”)
#import “RCTEventDispatcher.h”
#else
#import “React/RCTEventDispatcher.h” // Required when used as a Pod in a Swift project
#endif

#define NOTIFICATION_EVENT @"notification_on_receive"

@interface FirebasePushNotification ()
{
    
}
@end

@implementation FirebasePushNotification
@synthesize bridge = _bridge;


// Export a native module
// https://facebook.github.io/react-native/docs/native-modules-ios.html
RCT_EXPORT_MODULE();

+ (id)allocWithZone:(NSZone *)zone {
    static FirebasePushNotification *sharedInstance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [super allocWithZone:zone];
    });
    return sharedInstance;
}

RCT_EXPORT_METHOD(registerRemoteNotification:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject){
    dispatch_async(dispatch_get_main_queue(), ^{
        
        if ([UIApplication sharedApplication].isRegisteredForRemoteNotifications == YES) {
            [UNUserNotificationCenter currentNotificationCenter].delegate = self;
            resolve(nil);
        }else {
            if ([UNUserNotificationCenter class] != nil) {
                // iOS 10 or later
                // For iOS 10 display notification (sent via APNS)
                [UNUserNotificationCenter currentNotificationCenter].delegate = self;
                UNAuthorizationOptions authOptions = UNAuthorizationOptionAlert |
                UNAuthorizationOptionSound | UNAuthorizationOptionBadge;
                [[UNUserNotificationCenter currentNotificationCenter]
                 requestAuthorizationWithOptions:authOptions
                 completionHandler:^(BOOL granted, NSError * _Nullable error) {
                     if (granted) {
                         resolve(nil);
                     } else {
                         reject(@"messaging/permission_error", @"Failed to grant permission", error);
                     }
                 }];
            }
            [[UIApplication sharedApplication] registerForRemoteNotifications];
        }
    });
    
}

RCT_EXPORT_METHOD(subscribeToTopic:(NSString*) topic
                  resolve:(RCTPromiseResolveBlock) resolve
                  reject:(RCTPromiseRejectBlock) reject){
    
    [[FIRMessaging messaging] subscribeToTopic:topic];
    resolve(nil);
}

RCT_EXPORT_METHOD(unsubscribeFromTopic: (NSString*) topic
                  resolve:(RCTPromiseResolveBlock) resolve
                  reject:(RCTPromiseRejectBlock) reject) {
    [[FIRMessaging messaging] unsubscribeFromTopic:topic];
    resolve(nil);
}
- (void)userNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(UNNotificationPresentationOptions))completionHandler{
    UNNotificationPresentationOptions options = UNAuthorizationOptionAlert |
    UNAuthorizationOptionSound | UNAuthorizationOptionBadge;
    completionHandler(options);
}

- (void) sendEvent:(NSDictionary *)userInfo {
    [self sendEventWithName:NOTIFICATION_EVENT body:userInfo];
}

- (NSArray<NSString *> *)supportedEvents
{
    return @[NOTIFICATION_EVENT];
}

- (NSDictionary *)constantsToExport
{
    return @{@"NOTIFICATION_EVENT":NOTIFICATION_EVENT};
}

+ (BOOL)requiresMainQueueSetup {
    return YES;
}
@end

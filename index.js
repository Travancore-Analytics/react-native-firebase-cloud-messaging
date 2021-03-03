import { NativeModules, NativeEventEmitter } from "react-native";

const { FirebasePushNotification } = NativeModules;

export default {
  registerRemoteNotification() {
    return FirebasePushNotification.registerRemoteNotification()
  },
  subscribeToTopic(topic){
    return FirebasePushNotification.subscribeToTopic(topic)
  },
  unsubscribeFromTopic(topic){
    return FirebasePushNotification.unsubscribeFromTopic(topic)
  },
  onNotificationRecieve(eventName,callback){
    const eventEmitter = new NativeEventEmitter(Platform.OS === 'ios'? NativeModules.FirebasePushNotification : NativeModules.MainActivity);
    eventEmitter.addListener(
			eventName,
			(event) => {        
        callback(event);
			});
  },
  checkPermissionForPushNotification(callback) {
    return FirebasePushNotification.checkPermissionForPushNotification(callback);
  },
  getToken(callback) {
    return FirebasePushNotification.getToken(callback);
  }
};

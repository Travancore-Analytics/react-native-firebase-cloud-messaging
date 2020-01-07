import { NativeModules } from "react-native";

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
  }
};

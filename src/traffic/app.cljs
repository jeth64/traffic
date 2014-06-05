(ns traffic.app)


(defn  ^:export onDeviceReady []
  (-> (js* "navigator") (.-notification)
      (.alert "PhoneGap is working" (fn [] nil) "" "")))

(defn  ^:export initialize []
  (.addEventListener js/document "deviceready" onDeviceReady true))

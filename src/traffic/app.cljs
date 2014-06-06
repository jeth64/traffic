(ns traffic.app)


(defn ^:export prompt
  [msg]
  (try (-> (js* "navigator") (.-notification) (.alert msg (fn [] nil) "" ""))
       (catch :default e (js/alert msg))))


(defn ^:export onDeviceReady []
  (prompt "PhoneGap is working"))


(defn  ^:export initialize []
  (.addEventListener js/document "deviceready" onDeviceReady true))

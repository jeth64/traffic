(ns traffic.app
  (:require-macros [traffic.macros :as tm]))


;;
;; local storage
;;

;; general

(defn store [key value]
  (.setItem (.-localStorage js/window) key value))

(defn get-stored [key]
  (.getItem (.-localStorage js/window) key))

(defn rm-stored [key]
  (.removeItem (.-localStorage js/window) key))

(defn store-empty? []
  (= nil (.key (.-localStorage js/window) 0)))

(defn empty-store []
  (.clear (.-localStorage js/window) ))


;; specific

(defn fill-store
  "Parse and store traffic signs as list of maps"
  [target-key]
  (if (= nil (get-stored target-key))
    (store target-key (map #(zipmap [:title :bedeutung] %)
                           (tm/sign-list "resources/zeichenmap")))))


;;
;; Exported functions
;;

(defn ^:export prompt
  "Uses native notifications if possible"
  [msg]
  (try (-> (js* "navigator") (.-notification) (.alert msg (fn [] nil) "" ""))
       (catch :default e (js/alert msg))))


(defn ^:export onDeviceReady []
  (prompt "PhoneGap is working"))


(defn  ^:export initialize []
  (fill-store "traffic-signs" )
  (.addEventListener js/document "deviceready" onDeviceReady true))

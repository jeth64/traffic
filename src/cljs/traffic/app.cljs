(ns traffic.app
  (:require [traffic.storage :refer [fill-store]]
            [kioo.om :refer [content set-attr do-> substitute listen]]
            [kioo.core :refer [handle-wrapper]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true])
  (:require-macros [kioo.om :refer [defsnippet deftemplate]]))


;;
;; Exported functions
;;

(defn ^:export prompt
  "Uses native notifications if possible"
  [msg]
  (try (.alert (.-notification js/navigator) msg (fn [] nil) "" "")
       (catch :default e (js/alert msg))))


(defn ^:export onDeviceReady []
  (prompt "PhoneGap is working"))


(defn  ^:export initialize []
  (fill-store "traffic-signs" )
  (.addEventListener js/document "deviceready" onDeviceReady true))

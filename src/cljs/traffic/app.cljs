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

;;
;; page setup
;;

(defsnippet my-nav-item "main.html" [:.nav-item]
  [[caption func]]
  {[:a] (do-> (content caption)
              (listen :onClick #(func caption)))})

(defsnippet my-header "main.html" [:header]
  [{:keys [heading]}]
  {[:h1] (content heading)})

(defsnippet my-navigation "main.html" [:.content]
  [{:keys [navigation]}]
  {[:table] (content (map my-nav-item navigation))})


(deftemplate my-page "main.html"
  [data]
  {[:header] (substitute (my-header data))
   [:.content] (substitute (my-navigation data))})

(defn init [data]
  (initialize)
  (om/component (my-page data)))

(def app-state (atom {:heading "main"
                      :content "Hello World"
                      :navigation [["home" #(js/alert %)]
                                   ["next" #(js/alert %)]]}))

(om/root init app-state {:target  (.-body js/document)})

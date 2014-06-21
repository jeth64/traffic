(ns traffic.app
  (:require [traffic.storage :refer [fill-store empty-store get-stored]]
            [kioo.om :refer [content set-attr do-> substitute listen wrap]]
            [kioo.core :refer [handle-wrapper]]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.reader :refer [read-string]])
  (:require-macros [kioo.om :refer [defsnippet deftemplate]]))

(declare main-page main-state detail-page)

(def signmapname "traffic-signs")

;;
;; Exported functions
;;

(defn ^:export prompt
  "Uses native notifications if possible"
  [msg]
  (try (.alert (.-notification js/navigator) msg (fn [] nil) "" "")
       (catch :default e (js/alert msg))))


(defn ^:export onDeviceReady []
  (prompt "PhoneGap is working"
          ))


(defn  ^:export initialize []
  (fill-store signmapname)
  (.addEventListener js/document "deviceready" onDeviceReady true))

;;
;; shortcuts for kioo templating
;;


(defn jump-to [page state]
  (om/root #(om/component (page %)) state {:target (.-body js/document)}))



;;
;; sign page states
;;

(defn detail-state [section]
  (atom {:heading section
         :topic ""
         :list (filter #(= section (:kategorie %))
                       (read-string (get-stored signmapname)))}))

(defn text-search-result-state [search-str] ;; dummy state
  (atom {:heading "Suche"
         :topic (str "Ergebnisse für \"" search-str "\":")
         :list (filter #(= "-" (:kategorie %))
                       (read-string (get-stored signmapname)))}))

(def photo-search-result-state ;; [photo];; dummy state
  (atom {:heading "Suche"
         :topic "Ergebnisse der Fotosuche:"
         :list (filter #(= "-" (:kategorie %))
                       (read-string (get-stored signmapname)))}))

;;
;; signs page setup
;;

(defsnippet detail-item "detail.html" [:#sign-entry]
  [{:keys [url bedeutung]}]
  {[:#sign-img] (set-attr :src (str "zeichen/" url))
   [:#sign-meaning] (content bedeutung)})

(defsnippet detail-header "detail.html" [:#navigation]
  [{:keys [heading]}]
  {[:#back] (listen :onClick #(jump-to main-page main-state))
   [:#title] (content (str "    " heading))
   [:#text-search] (listen :onClick
                           #(jump-to detail-page
                                     (text-search-result-state
                                      (.-value (.getElementById
                                                js/document "text-search-text")))))
   [:#file-search] (listen :onClick #(jump-to detail-page photo-search-result-state))
   [:#photo-search] (listen :onClick #(jump-to detail-page photo-search-result-state))})

(defsnippet detail-list "detail.html" [:#content]
  [{:keys [list]}]
  {[:#sign-list] (content (map detail-item list))})

(deftemplate detail-page "detail.html"
  [data]
  {[:#navigation] (substitute (detail-header data))
   [:#topic] (content (:topic data))
   [:#content] (substitute (detail-list data))})


;;
;; main page setup
;;

(defsnippet main-nav-item "main.html" [:#nav-item]
  [caption]
  {[:a] (do-> (content caption)
              (listen :onClick #(jump-to detail-page (detail-state caption))))})


(defsnippet main-navigation "main.html" [:.content]
  [{:keys [navigation]}]
  {[:#navigation] (content (map main-nav-item navigation))})


(deftemplate main-page "main.html"
  [data]
  {[:#title] (content (:heading data))
   [:.content] (substitute (main-navigation data))})


(def main-state (atom {:heading "Verkehrszeichen"
                       :navigation ["Gefahrzeichen"
                                    "Vorschriftzeichen"
                                    "Richtzeichen"
                                    "Verkehrslenkungstafeln"
                                    "Verkehrseinrichtungen"
                                    "Zusatzzeichen"
                                    "Sinnbilder"
                                    "Autobahnschilder"]}))


(initialize)
(jump-to main-page main-state)

(ns traffic.storage
  (:require [clojure.string :refer [trim]])
  (:require-macros [traffic.macros :as tm]))


;;
;; local storage
;;

(defn decode-html-entity [str]
  (let [div (.createElement js/document "div")]
    (aset div "innerHTML" str)
    (.-nodeValue (.-firstChild div))))


;; general

(defn store [key value]
  (.setItem (.-localStorage js/window) key value))

(defn get-stored [key]
  (.getItem (.-localStorage js/window) key))

(defn rm-stored [key] ;; needed?
  (.removeItem (.-localStorage js/window) key))

(defn store-empty? [] ;; needed?
  (= nil (.key (.-localStorage js/window) 0)))

(defn empty-store [] ;; needed?
  (.clear (.-localStorage js/window)))



;; specific

(defn contains-re? [re target-str]
  (.test re target-str))

(defn create-record [[title meaning]]
  {:url (.replace (str title ".png") (js/RegExp. (.-source #" ") "g") "_")
   :bedeutung (decode-html-entity (trim meaning))
   :kategorie (cond (contains-re? #"Zusatzzeichen" title) "Zusatzzeichen"
                    (contains-re? #"Sinnbild" title) "Sinnbilder"
                    (contains-re? #"Zeichen( |_)1[0-9][0-9]([^0-9]|$)" title) "Gefahrzeichen"
                    (contains-re? #"Zeichen( |_)2[0-9][0-9]([^0-9]|$)" title) "Vorschriftzeichen"
                    (contains-re? #"(Z|z)eichen( |_)[3-4][0-9][0-9]([^0-9]|$)" title) "Richtzeichen"
                    (contains-re? #"Bundesautobahn 48" title) "Richtzeichen"
                    (contains-re? #"Zeichen( |_)5[0-9][0-9]([^0-9]|$)" title) "Verkehrslenkungstafeln"
                    (contains-re? #"Zeichen( |_)6[0-9][0-9]([^0-9]|$)" title) "Verkehrseinrichtungen"
                    (contains-re? #"RWB" title) "Autobahnschilder"
                    :else "-"
                    )})

(defn fill-store
  "Parse and store traffic signs as list of maps"
  [target-key]
  (if (= nil (get-stored target-key))
    (store target-key
           (map create-record (tm/sign-list "resources/zeichenmap")))))

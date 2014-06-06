(ns traffic.storage
  (:require-macros [traffic.macros :as tm]))


;;
;; local storage
;;


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

(defn fill-store
  "Parse and store traffic signs as list of maps"
  [target-key]
  (if (= nil (get-stored target-key))
    (store target-key (map #(zipmap [:title :bedeutung] %)
                           (tm/sign-list "resources/zeichenmap")))))

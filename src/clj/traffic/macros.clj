(ns traffic.macros
  (:require [clojure.string :refer [split split-lines trim]]))


(defmacro sign-list [relative-uri]
  "Reads and returns a template as a string."
  (mapv #(split (trim %) #"\.svg" 2) ;map not working? -> arity error
       (split-lines (slurp relative-uri))))

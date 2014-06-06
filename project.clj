(defproject traffic "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2173"]
                 [kioo "0.4.0"]
                 [om "0.5.1"]]

  :plugins [[lein-cljsbuild "1.0.1"]]

  :source-paths ["src/clj"]
  :resource-paths ["resources"]

  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs"]
                        :compiler {:output-to "assets/www/js/index.js"
                                   :optimizations :simple
                                   :pretty-print true
                                   :preamble ["react/react.js"]
                                   :externs ["react/externs/react.js"]}}
                       {:id "prod"
                        :source-paths ["src/cljs"]
                        :compiler {:output-to "assets/www/js/index.js"
                                   :optimizations :advanced
                                   :pretty-print false
                                   :preamble ["react/react.js"]
                                   :externs ["react/externs/react.js"]}}]})

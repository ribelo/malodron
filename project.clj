(defproject malodron "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [org.clojure/clojurescript "1.9.495" :scope "provided"]
                 [org.clojure/core.async "0.3.441"]
                 [com.stuartsierra/component "0.3.2"]
                 [com.taoensso/encore "2.90.1"]
                 [com.taoensso/timbre "4.8.0"]
                 [funcool/cuerdas "2.0.3"]
                 [cljs-ajax "0.5.9"]
                 [reagent "0.6.1"]
                 [re-frame "0.9.2"]
                 [day8.re-frame/http-fx "0.1.3"]
                 [com.andrewmcveigh/cljs-time "0.5.0-alpha2"]]

  :plugins [[lein-cljsbuild "1.1.4"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :figwheel {:http-server-root "public"
             :nrepl-port       7002
             :css-dirs         ["resources/public/css"]
             :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

  :profiles {:dev
             {:dependencies [[binaryage/devtools "0.8.2"]
                             [com.cemerick/piggieback "0.2.2-SNAPSHOT"]]
              :plugins      [[lein-figwheel "0.5.9"]]}}
  :cljsbuild {:builds
              [{:source-paths ["src/electron"]
                :id           "electron"
                :compiler     {:output-to      "resources/public/js/electron.js"
                               :optimizations  :simple
                               :pretty-print   true
                               :cache-analysis true}}
               {:id           "dev"
                :source-paths ["src/cljs"]
                :figwheel     {:on-jsload "malodron.core/mount-root"}
                :compiler     {:main                 malodron.core
                               :output-to            "resources/public/js/app.js"
                               :output-dir           "resources/public/js/out"
                               :asset-path           "js/out"
                               :source-map-timestamp true
                               :preloads             [devtools.preload]
                               :external-config      {:devtools/config {:features-to-install :all}}
                               :pretty-print         true}}
               {:id           "min"
                :source-paths ["src/cljs"]
                :compiler     {:main            malodron.core
                               :output-to       "resources/public/js/app.js"
                               :optimizations   :advanced
                               :closure-defines {goog.DEBUG false}
                               :pretty-print    false}}]}

  )

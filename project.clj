(defproject lib-noir-lite "0.7.8"
  :description "Libraries from Noir for your enjoyment."
  :url "https://github.com/noir-clojure/lib-noir"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring/ring-core "1.2.1"]
                 [clout "1.1.0"]
                 [org.mindrot/jbcrypt "0.3m"]
                 [compojure "1.1.6" :scope "provided"]
                 [cheshire "5.3.0" :scope "provided"]
                 [hiccup "1.0.4" :scope "provided"]
                 [ring-middleware-format "0.3.1" :scope "provided"]]
  :profiles {:provided {:dependencies [[compojure "1.1.6"]
                                       [cheshire "5.3.0"]
                                       [hiccup "1.0.4"]
                                       [ring-middleware-format "0.3.1"]]}}
  :plugins [[codox "0.6.6"]]
  :codox {:output-dir "doc"})

(defproject malista "0.1.0"
  :description "Match List and Statistics"
  :url "https://github.com/lenneman/malista"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [hiccup "1.0.5"]
                 [org.clojure/java.jdbc "0.6.0"]
                 [com.h2database/h2 "1.4.193"]
                 [ring/ring-jetty-adapter "1.6.1"]]
  :main malista.handler
  :plugins [[lein-ring "0.12.4"]]
  :ring {:handler malista.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})

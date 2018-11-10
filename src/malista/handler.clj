(ns malista.handler
  (:require [malista.views :as views]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
  (:gen-class))

(defroutes app-routes
  (GET "/"
       []
       (views/home-page))
  (GET "/add-new-player"
       []
       (views/add-new-player-page))
  (POST "/add-new-player"
        {params :params}
        (views/add-new-player-results-page params))
  (GET "/all-players"
       []
       (views/all-players-page))
  (GET "/all-results"
       []
       (views/all-results-page))
  (GET "/add-new-result"
       []
       (views/add-new-result-page))
  (POST "/add-new-result"
        {params :params}
        (views/add-new-result-results-page params))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))

(defn -main
  [& [port]]
  (let [port (Integer. (or port
                           (System/getenv "PORT")
                           5000))]
    (jetty/run-jetty #'app {:port  port
                            :join? false})))

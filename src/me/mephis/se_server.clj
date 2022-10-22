(ns me.mephis.se-server
  (:gen-class)
  (:require [clojure.pprint :as log])
  (:require [me.mephis.lib :as lib]
            [me.mephis.lib.env :refer [get-env]])
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :refer [response content-type]]
            [clojure.data.json :as json]
            [compojure.core :as compojure]
            [compojure.route :as compojure-route]))

;; constant
(def SERVER_PORT_KEY "SERVER_PORT")
(def SE_YAML_STORAGE_PATH "SE_YAML_STORAGE_PATH")

(defn provide-yaml-content [storage-path]
  #(slurp (str storage-path "/" (:name (:params %)))))

(defn modify-yaml-content [storage-path]
  #(spit (str storage-path "/" (:name (:params %)))
         (:data (json/read-str (:body %)))))

(defn wrap-resp-json [f]
  #(content-type (response
                  (json/write-str {:data (f %)}))
                "application/json"))

(compojure/defroutes app
  (compojure/PUT "/api/spec/:name" params
                 (->> (get-env "PWD")
                      (get-env SE_YAML_STORAGE_PATH)
                      provide-yaml-content
                      wrap-resp-json))
  (compojure/GET "/api/spec/:name" params
                 (->> (get-env "PWD")
                      (get-env SE_YAML_STORAGE_PATH)
                      provide-yaml-content
                      wrap-resp-json))
  (compojure-route/not-found "Page not found"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [port (lib/normalize-port
              (get-env SERVER_PORT_KEY 3000))]
    (log/pprint (str "Listen on " port))
    (jetty/run-jetty app
                     {:port port
                      :join? true})
    ))

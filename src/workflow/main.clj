(ns workflow.main
  (:require
   [clojure.java.io :as io] ;;reading from files and writing to files
   [integrant.core :as ig]
   [io.pedestal.http :as http]
   [io.pedestal.http.route :as route]))

(def config
  (-> "config.edn"
      io/resource ;;means look into resource folder for config.edn
      slurp ;;takes file object and converts it to edn
      ig/read-string)) ;;can change port if needed (we are not in this eg)

(defn respond-hello [_request]
  {:status 200 :body "Hello, everybody!"})

(def routes
  (route/expand-routes
   #{["/" :get respond-hello :route-name :home]}))

(defmethod ig/init-key ::service
  [_ {:keys [port] :as _options}] ;;can use options as a way to point to multiple keys
  (let [service-map {::http/join? false
                     ::http/port port ;;port is coming from config file
                     ::http/routes routes ;;can also define this in configfile
                     ::http/type :jetty}]
    {:server (-> service-map ;; key is server and value is our started server
                 http/create-server
                 http/start)}))
;;all of the data that has the info from our started server, can access it later
;;lives in the form of data, as the value of this hashmap with :server

(defmethod ig/halt-key! ::service
  [_ {:keys [server] :as _server-info}]
  (http/stop server))

(defn -main ;;telling java this is the entry point to our entire program
  []
  (ig/init config))

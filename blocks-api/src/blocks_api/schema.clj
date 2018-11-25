(ns blocks-api.schema
  "Contains custom resolvers and a function to provide the full schema."
  (:require
    [clojure.java.io :as io]
    [com.walmartlabs.lacinia.util :as util]
    [com.walmartlabs.lacinia.schema :as schema]
    [com.stuartsierra.component :as component]
    [clojure.edn :as edn]))

(def ^:private read-resource
  (comp edn/read-string slurp io/resource))

(defn resolve-user-paths
  [paths context args user]
  (let [{:keys [user_id]} args
        {:keys [id]} user]
    (filter (comp (partial = (Integer. (or user_id id))) :user_id) paths)))

(defn resolve-path-user [users-map c a path]
  (let [{:keys [user_id]} path]
      (get users-map user_id)))

(defn resolve-user [users-map c args v]
  (let [{:keys [id]} args]
    (get users-map (Integer. id))))

(defn- coll-to-map [xs] (reduce #(assoc %1 (:id %2) %2) {} xs))

(defn resolver-map
  [component]
  (let [data (read-resource "blocks-data.edn")
        paths (:paths data)
        users-map (->> data :users coll-to-map)]
    {:query/paths-by-user-id (partial resolve-user-paths paths)
     :query/user-by-id (partial resolve-user users-map)
     :Path/user (partial resolve-path-user users-map)
     :User/paths (partial resolve-user-paths paths)}))

(defn load-schema
  [component]
  (-> (read-resource "blocks-schema.edn")
      (util/attach-resolvers (resolver-map component))
      schema/compile))

(defrecord SchemaProvider [schema]
  
  component/Lifecycle
  
  (start [this]
    (assoc this :schema (load-schema this)))
  (stop [this]
    (assoc this :schema nil)))

(defn new-schema-provider
  []
  {:schema-provider (map->SchemaProvider {})})

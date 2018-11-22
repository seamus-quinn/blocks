(ns server.schema
  "Contains custom resolvers and a function to provide the full schema."
  (:require
    [clojure.java.io :as io]
    [com.walmartlabs.lacinia.util :as util]
    [com.walmartlabs.lacinia.schema :as schema]
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
      (get users-map user_id {:username "not found"})))

(defn coll-to-map [xs] (reduce #(assoc %1 (:id %2) %2) {} xs))

(defn resolver-map
  []
  (let [data (read-resource "blocks-data.edn")
        paths (:paths data)
        users-map (->> data :users coll-to-map)]
    {:query/paths-by-user-id (partial resolve-user-paths paths)
     :Path/user (partial resolve-path-user users-map)
     :User/paths (partial resolve-user-paths paths)}))

(defn load-schema
  []
  (-> (read-resource "blocks-schema.edn")
      (util/attach-resolvers (resolver-map))
      schema/compile))

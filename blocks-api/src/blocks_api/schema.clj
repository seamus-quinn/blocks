(ns blocks-api.schema
  "Contains custom resolvers and a function to provide the full schema."
  (:require
    [clojure.java.io :as io]
    [com.walmartlabs.lacinia.util :as util]
    [com.walmartlabs.lacinia.schema :as schema]
    [com.stuartsierra.component :as component]
    [blocks-api.db :as db]
    [clojure.edn :as edn]))

(defn resolve-user-paths
  [db]
  (fn [_ args user]
    (let [{:keys [user_id]} args
          {:keys [id]} user]
      (db/list-paths-for-user db (Integer. (or user_id id))))))

(defn resolve-path-user
  [db]
  (fn [c a path]
  (let [{:keys [user_id]} path]
      (db/find-user db user_id))))

(defn resolve-user
  [db]
  (fn  [c args v]
    (let [{:keys [id]} args]
      (db/find-user db id))))

(defn insert-user
  [db]
  (fn [c args v]
    (let [{:keys [username password]} args]
      (db/insert-user db {:username username :password password}))))

(defn- coll-to-map [xs] (reduce #(assoc %1 (:id %2) %2) {} xs))

(defn resolver-map
  [component]
  (let [db (:db component)]
    {:query/paths-by-user-id (resolve-user-paths db)
     :query/user-by-id (resolve-user db)
     :mutation/insert-user (insert-user db)
     :Path/user (resolve-path-user db)
     :User/paths (resolve-user-paths db)}))

(defn load-schema
  [component]
  (-> (io/resource "blocks-schema.edn")
      slurp
      edn/read-string
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
  {:schema-provider (-> {}
                        map->SchemaProvider
                        (component/using [:db]))})

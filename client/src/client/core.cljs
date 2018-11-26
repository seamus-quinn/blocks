(ns client.core
    (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(println "This text is printed from src/client/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:username ""
                          :password ""}))

(defn targ-val [e](-> e .-target .-value))

(defn set-input-val
  "takes an event and state and returns updated state"
  [p e s] 
  (assoc s p (targ-val e)))

(defn login []
  [:div
   [:h1 "Login"]
   [:form
    [:input {:placeholder "Username"
             :type :text
             :value (:username @app-state)
             :on-change (fn [e]
                          (swap!
                           app-state
                           (partial set-input-val :username e)))}]
    [:input {:placeholder "Password"
             :type :password
             :value (:password @app-state)
             :on-change (fn [e]
                          (swap!
                           app-state
                           (partial set-input-val :password e)))}]
    [:button {:type :submit} "Log In"]]])

(defn state-display []
  [:div
    [:h3 (:username @app-state)]
    [:h3 (:password @app-state)]
    [:h3 (str @app-state)]
  ]
)

(defn hello-world []
  [:div
   [:h1 "Welcome To Blocks"]
   [login]
   [state-display]
   ])



(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

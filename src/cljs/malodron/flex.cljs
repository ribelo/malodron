(ns malodron.flex
  (:require [reagent.core :as r]))


(defn display [style]
  (merge style {:display :flex}))

(defn row [style]
  (merge style {:flex-direction :row}))

(defn row-rewerse [style]
  (merge style {:flex-direction :row-rewerse}))

(defn column [style]
  (merge style {:flex-direction :column}))

(defn column-rewerse [style]
  (merge style {:flex-direction :column-rewerse}))

(defn wrap [style]
  (merge style {:flex-wrap :wrap}))

(defn nowrap [style]
  (merge style {:flex-wrap :nowrap}))

(defn justify [alignment style]
  (merge style {:justify-content alignment}))

(defn justify-start [style]
  (merge style {:justify-content :flex-start}))

(defn justify-end [style]
  (merge style {:justify-content :flex-end}))

(defn justify-center [style]
  (merge style {:justify-content :center}))


(defn hbox []
  (let [this (r/current-component)
        props (r/props this)
        children (r/children this)]
    (into [:div (r/merge-props {:style {:background-color "blue"}}
                               props)]
          children)))

(ns malodron.flex
  (:require [cljs.spec :as s]
            [reagent.core :as r]
            [cuerdas.core :as str]
            [taoensso.encore :refer [assoc-some]]))


(def ^:private all-prop-keys
  [:size :justify :align :self-align
   :min-width :width :max-width
   :min-height :height :max-height
   :color :gap :overflow
   :overflow-x :overflow-y])


(defn display [style]
  (merge style {:display :flex}))

(s/def :flex/direction #{"row" "row-rewerse" "column" "column-rewerse"})
(s/def :flex/wrap #{"wrap" "nowrap" nil})
(s/def :flex/flow #(let [[direction wrap] (str/split % " ")]
                     (or (and (s/valid? :flex/direction direction)
                              (s/valid? :flex/wrap wrap))
                         (s/valid? #{"inherit"} %))))

(defn flow [style flow]
  {:pre [(s/valid? :flex/flow flow)]}
  (merge style {:flex-flow flow}))

(defn justify-content [style alignment]
  (if alignment
    (merge style {:justify-content alignment})
    style))

(defn justify-content-start [style]
  (merge style {:justify-content :flex-start}))

(defn justify-content-end [style]
  (merge style {:justify-content :flex-end}))

(defn justify-content-center [style]
  (merge style {:justify-content :center}))

(defn justify-content-between [style]
  (merge style {:justify-content :space-between}))

(defn justify-content-around [style]
  (merge style {:justify-content :space-around}))

(defn align-items [style alignment]
  (if alignment
    (merge style {:align-items alignment})
    style))

(defn align-items-start [style]
  (merge style {:align-items :flex-start}))

(defn align-items-end [style]
  (merge style {:align-items :flex-end}))

(defn align-items-center [style]
  (merge style {:align-items :center}))

(defn align-items-stretch [style]
  (merge style {:align-items :stretch}))

(defn align-items-baseline [style]
  (merge style {:align-items :baseline}))

(defn align-self [style alignment]
  (if alignment
    (merge style {:align-self alignment})
    style))

(defn align-self-start [style]
  (merge style {:align-self :flex-start}))

(defn align-self-end [style]
  (merge style {:align-self :flex-end}))

(defn align-self-center [style]
  (merge style {:align-self :center}))

(defn align-self-stretch [style]
  (merge style {:align-self :stretch}))

(defn align-self-baseline [style]
  (merge style {:align-self :baseline}))

(defn align-content [style alignment]
  (if alignment
    (merge style {:align-content alignment})
    style))

(defn align-content-start [style]
  (merge style {:align-content :flex-start}))

(defn align-content-end [style]
  (merge style {:align-content :flex-end}))

(defn align-content-center [style]
  (merge style {:align-content :center}))

(defn align-content-stretch [style]
  (merge style {:align-content :stretch}))

(defn align-content-between [style]
  (merge style {:align-content :space-between}))

(defn align-content-around [style]
  (merge style {:align-content :space-around}))

(defn order [order style]
  (merge style {:order order}))


(s/def :flex/initial #{"initial" :initial})
(s/def :flex/auto #{"auto" :auto})
(s/def :flex/none #{"none" :none})
(s/def :flex/grow str/numeric?)
(s/def :flex/shrink str/numeric?)
(s/def :flex/basis (s/or :px #(re-find #"px" %)
                         :percent #(re-find #"\%" %)
                         :auto :flex/auto
                         :initial :flex/initial))
(s/def :flex/gsb #(let [[g s b] (str/split %)]
                    (and (s/valid? :flex/grow g)
                         (s/valid? :flex/shrink s)
                         (s/valid? :flex/basis b))))
(s/def :flex/flex (s/nilable
                    (s/or :auto :flex/auto
                          :initial :flex/initial
                          :none :flex/none
                          :grow :flex/grow
                          :gsb :flex/gsb
                          :basis :flex/basis)))


(defn flex [style size]
  (if size
    (let [[type size] (s/conform :flex/flex size)
          flex (case type
                 :initial "0 1 auto"
                 :auto "1 1 auto"
                 :none "0 0 auto"
                 :grow (str size " 1 0px")
                 :basis (let [[unit size] size]
                          (case unit
                            :px (str "0 0 " size)
                            :percent (str (re-find #"\d+" size) " 1 0px")))
                 size)]
      (merge style
             {:flex flex}))
    style))


(defn gap [{:keys [size width height]}]
  (let [this (r/current-component)
        props (apply dissoc (r/props this) all-prop-keys)
        children (r/children this)]
    (into [:div (r/merge-props {:style (-> {}
                                           (assoc-some :width width
                                                       :height height)
                                           (flex size))}
                               props)]
          children)))
(def gap_ gap)


(defn line [{:keys [size color]
             :or {color "lightgray"}}]
  (let [this (r/current-component)
        props (apply dissoc (r/props this) all-prop-keys)
        children (r/children this)]
    (into [:div (r/merge-props {:style (-> {:background-color color}
                                           (flex size))}
                               props)]
          children)))


(defn scroller [{:keys [size justify align self-align gap
                      min-width width max-width
                      min-height height max-height
                      overflow overflow-x overflow-y]
               :or {size "auto"}}]
  (let [this (r/current-component)
        props (apply dissoc (r/props this) all-prop-keys)
        children (r/children this)
        not-v-or-h (and (nil? overflow-y) (nil? overflow-x))
        overflow (if (and (nil? overflow) not-v-or-h) :auto overflow)]
    (into [:div (r/merge-props {:style (-> {}
                                           (assoc-some :min-width min-width
                                                       :width width
                                                       :max-width max-width
                                                       :min-height min-height
                                                       :height height
                                                       :max-height max-height
                                                       :overflow overflow
                                                       :overflow-x overflow-x
                                                       :overflow-y overflow-y)
                                           (display)
                                           (flex size)
                                           (flow "inherit")
                                           (justify-content justify)
                                           (align-items align)
                                           (align-self self-align))}
                               props)]
          children)))


(defn hbox [{:keys [size justify align self-align gap
                    min-width width max-width
                    min-height height max-height]
             :or   {justify :start align :stretch size :none}}]
  (let [this (r/current-component)
        props (apply dissoc (r/props this) all-prop-keys)
        gap-form (when gap [gap_ {:size gap}])
        children (if gap
                   (interpose gap-form (filter identity (r/children this)))
                   (r/children this))]
    (into [:div (r/merge-props {:style (-> {}
                                           (assoc-some :min-width min-width
                                                       :width width
                                                       :max-width max-width
                                                       :min-height min-height
                                                       :height height
                                                       :max-height max-height)
                                           (display)
                                           (flex size)
                                           (flow "row nowrap")
                                           (justify-content justify)
                                           (align-items align)
                                           (align-self self-align))}
                               props)]
          children)))


(defn vbox [{:keys [size justify align self-align gap
                    min-width width max-width
                    min-height height max-height]
             :or   {justify :start align :stretch size :none}}]
  (let [this (r/current-component)
        props (apply dissoc (r/props this) all-prop-keys)
        gap-form (when gap [gap_ {:size gap}])
        children (if gap
                   (interpose gap-form (filter identity (r/children this)))
                   (r/children this))]
    (into [:div (r/merge-props {:style (-> {}
                                           (assoc-some :min-width min-width
                                                       :width width
                                                       :max-width max-width
                                                       :min-height min-height
                                                       :height height
                                                       :max-height max-height)
                                           (display)
                                           (flex size)
                                           (flow "column nowrap")
                                           (justify-content justify)
                                           (align-items align)
                                           (align-self self-align))}
                               props)]
          children)))


(defn box [{:keys [size justify align self-align gap
                   min-width width max-width
                   min-height height max-height]
            :or   {justify :start align :stretch size :none}}]
  (let [this (r/current-component)
        props (apply dissoc (r/props this) all-prop-keys)
        gap-form (when gap [gap_ {:size gap}])
        children (if gap
                   (interpose gap-form (filter identity (r/children this)))
                   (r/children this))]
    (into [:div (r/merge-props {:style (-> {}
                                           (assoc-some :min-width min-width
                                                       :width width
                                                       :max-width max-width
                                                       :min-height min-height
                                                       :height height
                                                       :max-height max-height)
                                           (display)
                                           (flex size)
                                           (flow "inherit")
                                           (justify-content justify)
                                           (align-items align)
                                           (align-self self-align))}
                               props)]
          children)))

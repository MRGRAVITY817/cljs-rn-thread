(ns components.flat-list
  (:require [react-native :as rn]
            [uix.core :refer [defui $]]))

(defui flat-list
  "A FlatList component that renders a list of items."
  [{:keys [data key-extractor render-item] :as props}]
  ($ rn/FlatList (merge props
                        {:data (clj->js data)
                         :key-extractor (fn [item]
                                          (let [item (js->clj item :keywordize-keys true)]
                                            (key-extractor item)))
                         :render-item (fn [props]
                                        (let [item (js->clj (.-item props) :keywordize-keys true)]
                                          (render-item item)))})))

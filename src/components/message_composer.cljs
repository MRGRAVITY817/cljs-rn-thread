(ns components.message-composer
  (:require [uix.core :as uix :refer [$ defui]]
            [react-native :as rn]))

(defui message-composer
  [{:keys [on-send]}]
  (let [[message-text set-message-text] (uix/use-state "")
        can-send (> (count (clojure.string/trim message-text)) 0)]

    ($ rn/View
       {:style {:flex-direction "row"
                :align-items "flex-end"
                :padding-horizontal 16
                :padding-vertical 12
                :background-color "white"
                :border-top-width 1
                :border-top-color "#E5E5EA"}}

       ($ rn/View
          {:style {:flex 1
                   :margin-right 12}}

          ($ rn/TextInput
             {:style {:border-width 1
                      :border-color "#E5E5EA"
                      :border-radius 20
                      :padding-horizontal 16
                      :padding-vertical 8
                      :font-size 16
                      :line-height 20
                      :max-height 120
                      :min-height 36}
              :value message-text
              :on-change-text set-message-text
              :placeholder "Type a message..."
              :placeholder-text-color "#8E8E93"
              :multiline true
              :text-align-vertical "center"
              :return-key-type "send"
              :on-submit-editing (fn []
                                   (when can-send
                                     (on-send message-text)
                                     (set-message-text "")))}))

       ($ rn/TouchableOpacity
          {:style (merge {:width 36
                          :height 36
                          :border-radius 18
                          :justify-content "center"
                          :align-items "center"}
                         (if can-send
                           {:background-color "#007AFF"}
                           {:background-color "#E5E5EA"}))
           :disabled (not can-send)
           :on-press (fn []
                       (when can-send
                         (on-send message-text)
                         (set-message-text "")))}

          ($ rn/Text
             {:style {:color (if can-send "white" "#8E8E93")
                      :font-size 16
                      :font-weight "600"}}
             "↑")))))
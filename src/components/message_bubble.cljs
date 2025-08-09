(ns components.message-bubble
  (:require [uix.core :as uix :refer [$]]
            [react-native :as rn]
            [app.data :refer [users]]))

(defn format-message-time [timestamp]
  (let [date (js/Date. timestamp)
        hours (.getHours date)
        minutes (.getMinutes date)
        formatted-minutes (if (< minutes 10) (str "0" minutes) minutes)
        am-pm (if (< hours 12) "AM" "PM")
        formatted-hours (if (= hours 0) 12
                            (if (> hours 12) (- hours 12) hours))]
    (str formatted-hours ":" formatted-minutes " " am-pm)))

(defui message-bubble
  [{:keys [message current-user is-last-in-group]}]
  (let [is-own-message (= (:sender message) current-user)
        sender-user (first (filter #(= (:username %) (:sender message)) users))]
    ($ rn/view
       {:style (cond-> {:flex-direction "row"
                        :margin-horizontal 12
                        :margin-bottom (if is-last-in-group 12 4)}
                 is-own-message (assoc :justify-content "flex-end"))}

       (when (and (not is-own-message) is-last-in-group)
         ($ rn/image
            {:source {:uri (:avatar sender-user)}
             :style {:width 32
                     :height 32
                     :border-radius 16
                     :margin-right 8
                     :align-self "flex-end"}}))

       (when (and (not is-own-message) (not is-last-in-group))
         ($ rn/view {:style {:width 32 :margin-right 8}}))

       ($ rn/view
          {:style {:flex-shrink 1
                   :max-width "80%"}}

          ($ rn/view
             {:style (merge {:padding-horizontal 16
                             :padding-vertical 12
                             :border-radius 20}
                            (if is-own-message
                              {:background-color "#007AFF"
                               :align-self "flex-end"}
                              {:background-color "#F2F2F7"
                               :align-self "flex-start"}))}

             ($ rn/text
                {:style (merge {:font-size 16
                                :line-height 20}
                               (when is-own-message
                                 {:color "white"}))}
                (:content message)))

          (when is-last-in-group
            ($ rn/text
               {:style {:font-size 12
                        :color "#8E8E93"
                        :margin-top 4
                        :align-self (if is-own-message "flex-end" "flex-start")}}
               (format-message-time (:timestamp message))))))))
(ns app.messages
  (:require [uix.core :as uix :refer [$]]
            [react-native :as rn]
            [components.flat-list :refer [flat-list]]
            [app.data :refer [conversations users]]))

(defn format-conversation-time [timestamp]
  (let [now (js/Date.)
        message-date (js/Date. timestamp)
        diff-ms (- (.getTime now) (.getTime message-date))
        diff-hours (/ diff-ms 1000 60 60)
        diff-days (Math/floor (/ diff-hours 24))]
    (cond
      (< diff-hours 1) "now"
      (< diff-hours 24) (str (Math/floor diff-hours) "h")
      (< diff-days 7) (str diff-days "d")
      :else (.toLocaleDateString message-date))))

(defn get-conversation-partner [conversation current-user]
  (let [partner-username (first (filter #(not= % current-user) (:participants conversation)))]
    (first (filter #(= (:username %) partner-username) users))))

(defui conversation-item
  [{:keys [conversation current-user on-select]}]
  (let [partner (get-conversation-partner conversation current-user)]
    ($ rn/touchable-opacity
       {:style {:flex-direction "row"
                :align-items "center"
                :padding-horizontal 16
                :padding-vertical 12
                :border-bottom-width 1
                :border-bottom-color "#F2F2F7"}
        :on-press #(on-select conversation)}

       ($ rn/image
          {:source {:uri (:avatar partner)}
           :style {:width 50
                   :height 50
                   :border-radius 25
                   :margin-right 12}})

       ($ rn/view
          {:style {:flex 1}}

          ($ rn/view
             {:style {:flex-direction "row"
                      :justify-content "space-between"
                      :align-items "center"
                      :margin-bottom 4}}

             ($ rn/text
                {:style {:font-size 16
                         :font-weight "600"
                         :color "#000"}}
                (:name partner))

             ($ rn/text
                {:style {:font-size 14
                         :color "#8E8E93"}}
                (format-conversation-time (:timestamp conversation))))

          ($ rn/view
             {:style {:flex-direction "row"
                      :justify-content "space-between"
                      :align-items "center"}}

             ($ rn/text
                {:style {:font-size 14
                         :color "#8E8E93"
                         :flex 1}
                 :number-of-lines 1}
                (:last-message conversation))

             (when (> (:unread-count conversation) 0)
               ($ rn/view
                  {:style {:background-color "#007AFF"
                           :border-radius 10
                           :width 20
                           :height 20
                           :justify-content "center"
                           :align-items "center"
                           :margin-left 8}}
                  ($ rn/text
                     {:style {:color "white"
                              :font-size 12
                              :font-weight "600"}}
                     (:unread-count conversation)))))))))

(defui messages-header
  [{:keys [on-back]}]
  ($ rn/view
     {:style {:flex-direction "row"
              :align-items "center"
              :justify-content "space-between"
              :padding-horizontal 16
              :padding-vertical 12
              :background-color "white"
              :border-bottom-width 1
              :border-bottom-color "#E5E5EA"}}

     ($ rn/touchable-opacity
        {:on-press on-back
         :style {:padding 8
                 :margin -8}}
        ($ rn/text
           {:style {:font-size 18
                    :color "#007AFF"}}
           "‹ Back"))

     ($ rn/text
        {:style {:font-size 18
                 :font-weight "600"
                 :color "#000"}}
        "Messages")

     ($ rn/view {:style {:width 32}})))

(defui messages-screen
  [{:keys [current-user on-back on-select-conversation]}]
  (let [sorted-conversations (sort-by #(.getTime (js/Date. (:timestamp %))) > conversations)]
    ($ rn/view
       {:style {:flex 1
                :background-color "white"}}

       ($ messages-header
          {:on-back on-back})

       (if (empty? sorted-conversations)
         ($ rn/view
            {:style {:flex 1
                     :justify-content "center"
                     :align-items "center"
                     :padding 32}}
            ($ rn/text
               {:style {:font-size 18
                        :color "#8E8E93"
                        :text-align "center"}}
               "No messages yet")
            ($ rn/text
               {:style {:font-size 14
                        :color "#8E8E93"
                        :text-align "center"
                        :margin-top 8}}
               "Start a conversation to see your messages here"))

         ($ flat-list
            {:data sorted-conversations
             :key-extractor #(:id %)
             :render-item (fn [item]
                            ($ conversation-item
                               {:conversation item
                                :current-user current-user
                                :on-select on-select-conversation}))})))))
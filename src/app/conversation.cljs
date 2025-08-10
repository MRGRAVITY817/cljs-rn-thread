(ns app.conversation
  (:require
   [app.data :refer [messages users]]
   [components.flat-list :refer [flat-list]]
   [components.message-bubble :refer [message-bubble]]
   [components.message-composer :refer [message-composer]]
   [react-native :as rn]
   [uix.core :as uix :refer [$ defui]]))

(defn get-conversation-partner [conversation current-user]
  (let [partner-username (first (filter #(not= % current-user) (:participants conversation)))]
    (first (filter #(= (:username %) partner-username) users))))

(defn group-messages-by-sender [messages]
  (loop [grouped []
         remaining messages
         current-group []]
    (if (empty? remaining)
      (if (empty? current-group)
        grouped
        (conj grouped current-group))
      (let [current-message (first remaining)
            rest-messages (rest remaining)]
        (if (or (empty? current-group)
                (= (:sender current-message) (:sender (first current-group))))
          (recur grouped rest-messages (conj current-group current-message))
          (recur (conj grouped current-group) rest-messages [current-message]))))))

(defui conversation-header
  [{:keys [conversation current-user on-back]}]
  (let [partner (get-conversation-partner conversation current-user)]
    ($ rn/SafeAreaView {:style {:background-color "white"
                                :border-bottom-width 0.5
                                :border-bottom-color "#e1e8ed"}}
       ($ rn/View
          {:style {:flex-direction "row"
                   :align-items "center"
                   :padding-horizontal 16
                   :padding-vertical 12
                   :background-color "white"
                   :border-bottom-width 1
                   :border-bottom-color "#E5E5EA"}}

          ($ rn/TouchableOpacity
             {:on-press on-back
              :style {:padding 8
                      :margin -8
                      :margin-right 4}}
             ($ rn/Text
                {:style {:font-size 18
                         :color "#007AFF"}}
                "‹"))

          ($ rn/Image
             {:source {:uri (:avatar partner)}
              :style {:width 32
                      :height 32
                      :border-radius 16
                      :margin-right 12}})

          ($ rn/View
             {:style {:flex 1}}
             ($ rn/Text
                {:style {:font-size 16
                         :font-weight "600"
                         :color "#000"}}
                (:name partner))
             ($ rn/Text
                {:style {:font-size 14
                         :color "#8E8E93"}}
                (str "@" (:username partner))))))))

(defui conversation-screen
  [{:keys [conversation current-user on-back]}]
  (let [conversation-messages (get messages (:id conversation) [])
        [local-messages set-local-messages] (uix/use-state conversation-messages)
        grouped-messages (group-messages-by-sender local-messages)

        handle-send-message (fn [content]
                              (let [new-message {:id (str "msg-" (random-uuid))
                                                 :sender current-user
                                                 :content content
                                                 :timestamp (.toISOString (js/Date.))
                                                 :read true}]
                                (set-local-messages #(conj % new-message))))]

    ($ rn/View
       {:style {:flex 1
                :background-color "#F8F9FA"}}

       ($ conversation-header
          {:conversation conversation
           :current-user current-user
           :on-back on-back})

       (if (empty? local-messages)
         ($ rn/View
            {:style {:flex 1
                     :justify-content "center"
                     :align-items "center"
                     :padding 32}}
            ($ rn/Text
               {:style {:font-size 18
                        :color "#8E8E93"
                        :text-align "center"}}
               "No messages yet")
            ($ rn/Text
               {:style {:font-size 14
                        :color "#8E8E93"
                        :text-align "center"
                        :margin-top 8}}
               "Say hello to start the conversation!"))

         ($ flat-list
            {:style {:flex 1}
             :data (mapcat (fn [group]
                             (map-indexed (fn [idx msg]
                                            (assoc msg :is-last-in-group (= idx (dec (count group)))))
                                          group))
                           grouped-messages)
             :key-extractor #(:id %)
             :render-item (fn [message]
                            ($ message-bubble
                               {:message message
                                :current-user current-user
                                :is-last-in-group (:is-last-in-group message)}))
             :inverted false
             :content-container-style {:padding-vertical 8}}))

       ($ message-composer
          {:on-send handle-send-message}))))

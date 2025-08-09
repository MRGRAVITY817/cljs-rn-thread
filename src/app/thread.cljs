(ns app.thread
  (:require [react-native :as rn]
            [uix.core :refer [$ defui] :as uix]
            [app.data :refer [mock-posts]]
            [components.feed-item :refer [feed-item]]))

(defn get-thread-posts [thread-id posts]
  "Get all posts in a thread, sorted by creation order"
  (->> posts
       (filter #(= (:thread-id %) thread-id))
       (sort-by :id)))

(defn get-main-post [thread-id posts]
  "Get the main post that started the thread"
  (->> posts
       (filter #(and (= (:thread-id %) thread-id)
                     (nil? (:reply-to %))))
       (first)))

(defn get-replies [parent-id posts]
  "Get direct replies to a specific post"
  (->> posts
       (filter #(= (:reply-to %) parent-id))
       (sort-by :id)))

(defui thread-connector [{:keys [is-last?]}]
  "Visual connector line for thread replies"
  ($ rn/View {:style {:width 2
                      :background-color "#e1e8ed"
                      :margin-left 19
                      :margin-top 4
                      :height (if is-last? 20 48)}}))

(defui thread-item [{:keys [post on-like on-reply on-repost on-profile-click replies level]}]
  "Individual post in a thread with connector lines"
  (let [has-replies? (> (count replies) 0)]
    ($ rn/View {:style {:flex 1}}

       ;; Main post
       ($ rn/View {:style {:flex-direction "row"}}
          ;; Thread connector for replies (not main post)
          (when (> level 0)
            ($ rn/View {:style {:width 40
                                :align-items "center"
                                :padding-top 12}}
               ($ rn/View {:style {:width 2
                                   :height 20
                                   :background-color "#e1e8ed"}})))

          ;; Post content
          ($ rn/View {:style {:flex 1
                              :margin-left (if (> level 0) 8 0)}}
             ($ feed-item {:post post
                           :on-like on-like
                           :on-reply on-reply
                           :on-repost on-repost
                           :on-profile-click on-profile-click})))

       ;; Replies (nested)
       (when has-replies?
         ($ rn/View {:style {:margin-left 40}}
            (for [reply replies]
              ($ thread-item {:key (:id reply)
                              :post reply
                              :on-like on-like
                              :on-reply on-reply
                              :on-repost on-repost
                              :on-profile-click on-profile-click
                              :replies (get-replies (:id reply) mock-posts)
                              :level (inc level)})))))))

(defui thread-header [{:keys [on-back main-post]}]
  "Header for thread view"
  ($ rn/SafeAreaView {:style {:background-color "white"
                              :border-bottom-width 0.5
                              :border-bottom-color "#e1e8ed"}}
     ($ rn/View {:style {:flex-direction "row"
                         :align-items "center"
                         :padding-horizontal 16
                         :padding-vertical 12}}
        ($ rn/TouchableOpacity {:on-press on-back
                                :style {:padding 8
                                        :margin-right 12}}
           ($ rn/Text {:style {:font-size 18}} "←"))
        ($ rn/View {:style {:flex 1}}
           ($ rn/Text {:style {:font-size 20
                               :font-weight "bold"
                               :color "#0f1419"}}
              "Thread")
           (when main-post
             ($ rn/Text {:style {:font-size 13
                                 :color "#536471"}}
                (str "Started by " (get-in main-post [:user :name]))))))))

(defui thread-screen [{:keys [thread-id on-back on-profile-click on-reply]}]
  "Main thread view screen"
  (let [thread-posts (get-thread-posts thread-id mock-posts)
        main-post (get-main-post thread-id mock-posts)
        [posts set-posts!] (uix/use-state thread-posts)

        handle-like (uix/use-callback
                     (fn [post-id]
                       (set-posts! (fn [current-posts]
                                     (mapv (fn [post]
                                             (if (= (:id post) post-id)
                                               (-> post
                                                   (update :liked? not)
                                                   (update :likes (if (:liked? post) dec inc)))
                                               post))
                                           current-posts))))
                     [])

        handle-reply (uix/use-callback
                      (fn [post-id]
                        (when on-reply
                          (on-reply post-id)))
                      [on-reply])

        handle-repost (uix/use-callback
                       (fn [post-id]
                         (js/console.log "Repost" post-id))
                       [])]

    (if (empty? posts)
      ;; Empty state
      ($ rn/View {:style {:flex 1
                          :background-color "#f7f9fa"}}
         ($ thread-header {:on-back on-back
                           :main-post nil})
         ($ rn/View {:style {:flex 1
                             :justify-content "center"
                             :align-items "center"}}
            ($ rn/Text {:style {:font-size 18
                                :color "#536471"}}
               "Thread not found")))

      ;; Thread content
      ($ rn/View {:style {:flex 1
                          :background-color "#f7f9fa"}}
         ($ thread-header {:on-back on-back
                           :main-post main-post})

         ($ rn/ScrollView {:style {:flex 1}
                           :content-container-style {:padding-bottom 20}}

            ;; Main post
            (when main-post
              ($ thread-item {:post main-post
                              :on-like handle-like
                              :on-reply handle-reply
                              :on-repost handle-repost
                              :on-profile-click on-profile-click
                              :replies (get-replies (:id main-post) posts)
                              :level 0})))))))
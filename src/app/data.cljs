(ns app.data)

(def mock-posts
  [{:id 1
    :user {:name "Sarah Chen"
           :username "@sarahc"
           :avatar "https://via.placeholder.com/40"}
    :content "Just shipped a new feature at work! #ReactNative + #ClojureScript is such a powerful combo 🚀 #MobileDev"
    :timestamp "2h"
    :likes 24
    :replies 5
    :reposts 3
    :liked? false
    :reply-to nil
    :thread-id 1}

   {:id 2
    :user {:name "Alex Rivera"
           :username "@alexr"
           :avatar "https://via.placeholder.com/40"}
    :content "Hot take: #ClojureScript's immutable data structures make mobile development so much more predictable. No more mysterious state bugs! 🐛 #FunctionalProgramming #MobileDev"
    :timestamp "4h"
    :likes 67
    :replies 12
    :reposts 8
    :liked? true
    :reply-to nil
    :thread-id 2}

   {:id 3
    :user {:name "Maya Patel"
           :username "@mayap"
           :avatar "https://via.placeholder.com/40"}
    :content "Anyone else obsessed with the new iOS 17 design patterns? The subtle animations are *chef's kiss* 👌 #MobileDev #iOSDev"
    :timestamp "6h"
    :likes 89
    :replies 23
    :reposts 15
    :liked? false
    :reply-to nil
    :thread-id 3}

   {:id 4
    :user {:name "Jordan Kim"
           :username "@jordank"
           :avatar "https://via.placeholder.com/40"}
    :content "Debugging a tricky race condition today. Sometimes the best solution is to step away and come back with fresh eyes. What's your debugging ritual? #MobileDev #Debugging"
    :timestamp "8h"
    :likes 42
    :replies 18
    :reposts 6
    :liked? true
    :reply-to nil
    :thread-id 4}

   {:id 5
    :user {:name "Emma Watson"
           :username "@emmaw"
           :avatar "https://via.placeholder.com/40"}
    :content "Learning Clojure has completely changed how I think about programming. The emphasis on simplicity and composability is beautiful. #ClojureScript #FunctionalProgramming"
    :timestamp "12h"
    :likes 156
    :replies 34
    :reposts 28
    :liked? false
    :reply-to nil
    :thread-id 5}

   {:id 6
    :user {:name "Carlos Mendez"
           :username "@carlosm"
           :avatar "https://via.placeholder.com/40"}
    :content "Pro tip: Use shadow-cljs with #ReactNative for the best hot reloading experience. Game changer for productivity! ⚡ #ClojureScript #MobileDev"
    :timestamp "1d"
    :likes 203
    :replies 45
    :reposts 67
    :liked? true
    :reply-to nil
    :thread-id 6}

   {:id 7
    :user {:name "Zoe Taylor"
           :username "@zoet"
           :avatar "https://via.placeholder.com/40"}
    :content "Working on a new design system. The challenge isn't creating components, it's creating the right abstractions that scale. #DesignSystems #MobileDev"
    :timestamp "1d"
    :likes 78
    :replies 15
    :reposts 12
    :liked? false
    :reply-to nil
    :thread-id 7}

   {:id 8
    :user {:name "Ryan Murphy"
           :username "@ryanm"
           :avatar "https://via.placeholder.com/40"}
    :content "Just finished reading 'The Joy of Clojure'. Mind = blown 🤯 The chapter on macros especially. Time to refactor everything! #ClojureScript #FunctionalProgramming"
    :timestamp "2d"
    :likes 91
    :replies 21
    :reposts 19
    :liked? false
    :reply-to nil
    :thread-id 8}

   ;; Sample replies
   {:id 9
    :user {:name "Tech Enthusiast"
           :username "@techlover"
           :avatar "https://via.placeholder.com/40"}
    :content "@sarahc Totally agree! The REPL-driven development is amazing. How are you handling state management? #ClojureScript #ReactNative"
    :timestamp "1h"
    :likes 5
    :replies 2
    :reposts 0
    :liked? false
    :reply-to 1
    :thread-id 1}

   {:id 10
    :user {:name "Dev Community"
           :username "@devs"
           :avatar "https://via.placeholder.com/40"}
    :content "@alexr This is exactly why I switched from JavaScript! The confidence you get from immutability is unmatched. #FunctionalProgramming #ClojureScript"
    :timestamp "3h"
    :likes 12
    :replies 3
    :reposts 1
    :liked? false
    :reply-to 2
    :thread-id 2}

   {:id 11
    :user {:name "Sarah Chen"
           :username "@sarahc"
           :avatar "https://via.placeholder.com/40"}
    :content "@techlover We're using Re-frame for global state and UIx hooks for local component state. Works really well together! #ClojureScript #StateManagement"
    :timestamp "45m"
    :likes 8
    :replies 0
    :reposts 0
    :liked? false
    :reply-to 9
    :thread-id 1}

   {:id 12
    :user {:name "Mobile Designer"
           :username "@mobileui"
           :avatar "https://via.placeholder.com/40"}
    :content "@mayap YES! The new navigation transitions are so smooth. Have you tried implementing them in #ReactNative yet? #MobileDev #iOSDev"
    :timestamp "5h"
    :likes 15
    :replies 1
    :reposts 2
    :liked? true
    :reply-to 3
    :thread-id 3}

   ;; Additional posts for better hashtag coverage
   {:id 13
    :user {:name "Open Source Dev"
           :username "@osdev"
           :avatar "https://via.placeholder.com/40"}
    :content "Contributing to #OpenSource projects has taught me more than any course ever could. The community is amazing! 🌟 #Programming #ClojureScript"
    :timestamp "3d"
    :likes 67
    :replies 8
    :reposts 12
    :liked? false
    :reply-to nil
    :thread-id 13}

   {:id 14
    :user {:name "Code Mentor"
           :username "@codementor"
           :avatar "https://via.placeholder.com/40"}
    :content "Teaching junior developers about #FunctionalProgramming concepts. The 'aha!' moments are the best part of mentoring! 💡 #ClojureScript #Teaching"
    :timestamp "4d"
    :likes 89
    :replies 15
    :reposts 6
    :liked? true
    :reply-to nil
    :thread-id 14}

   {:id 15
    :user {:name "React Expert"
           :username "@reactpro"
           :avatar "https://via.placeholder.com/40"}
    :content "Loving the new #ReactNative architecture! The performance improvements are noticeable. Anyone else upgrading their apps? #MobileDev #Performance"
    :timestamp "5d"
    :likes 134
    :replies 28
    :reposts 19
    :liked? false
    :reply-to nil
    :thread-id 15}])

(def users
  {"@sarahc" {:name "Sarah Chen"
              :username "@sarahc"
              :avatar "https://via.placeholder.com/40"
              :bio "Senior Frontend Developer @TechCorp. React Native + ClojureScript enthusiast. Always learning. 🚀"
              :location "San Francisco, CA"
              :joined "March 2020"
              :followers 1247
              :following 892
              :posts 324}

   "@alexr" {:name "Alex Rivera"
             :username "@alexr"
             :avatar "https://via.placeholder.com/40"
             :bio "Full-stack engineer. Functional programming advocate. ClojureScript fanatic. Coffee addict. ☕"
             :location "Austin, TX"
             :joined "June 2019"
             :followers 2156
             :following 456
             :posts 789}

   "@mayap" {:name "Maya Patel"
             :username "@mayap"
             :avatar "https://via.placeholder.com/40"
             :bio "iOS Designer & Developer. Crafting beautiful mobile experiences. Design systems enthusiast. 🎨"
             :location "New York, NY"
             :joined "January 2021"
             :followers 3421
             :following 234
             :posts 512}

   "@jordank" {:name "Jordan Kim"
               :username "@jordank"
               :avatar "https://via.placeholder.com/40"
               :bio "Backend Engineer. Distributed systems & microservices. Debugging detective. 🔍"
               :location "Seattle, WA"
               :joined "September 2018"
               :followers 987
               :following 321
               :posts 435}

   "@emmaw" {:name "Emma Watson"
             :username "@emmaw"
             :avatar "https://via.placeholder.com/40"
             :bio "Software Engineer & Tech Writer. Passionate about functional programming and clean code. 📝"
             :location "London, UK"
             :joined "December 2020"
             :followers 5432
             :following 178
             :posts 267}

   "@carlosm" {:name "Carlos Mendez"
               :username "@carlosm"
               :avatar "https://via.placeholder.com/40"
               :bio "DevOps Engineer & Open Source Contributor. CI/CD wizard. Always optimizing. ⚡"
               :location "Barcelona, Spain"
               :joined "April 2019"
               :followers 1876
               :following 543
               :posts 398}

   "@zoet" {:name "Zoe Taylor"
            :username "@zoet"
            :avatar "https://via.placeholder.com/40"
            :bio "Design System Architect. Component libraries & design tokens. Creating scalable design. 🎯"
            :location "Toronto, Canada"
            :joined "August 2021"
            :followers 2943
            :following 412
            :posts 156}

   "@ryanm" {:name "Ryan Murphy"
             :username "@ryanm"
             :avatar "https://via.placeholder.com/40"
             :bio "Clojure Developer & Consultant. Lisp lover. Building elegant solutions with immutable data. 🔄"
             :location "Portland, OR"
             :joined "November 2017"
             :followers 1654
             :following 289
             :posts 623}})
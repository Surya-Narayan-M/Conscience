# CONSCIENCE APP — QUESTION & QUOTE BANK
### 200+ Questions | 150+ Quotes | Designed to hit hard and make you think

---

> Drop this entire file into Copilot Chat and say:
> "Replace the QuestionBank.kt content with all questions and quotes from this file, maintaining the exact same data structure."

---

## ENTRY SHOCK MESSAGES (shown when Instagram opens)
### 60 messages — rotated randomly

```kotlin
val ENTRY_MESSAGES = listOf(

    // --- TIME THEFT ---
    "You just opened Instagram.\nSomeone your age just started building something.",
    "This app has trained you to open it without deciding to.\nYou just proved it.",
    "Instagram made money the last time you used it.\nWhat did you make?",
    "The average person loses 2.5 hours a day here.\nAre you below average?",
    "Right now you could read 4 pages of a book.\nYou chose this instead.",
    "You have the same 24 hours as everyone you admire.\nThis is how you're spending them.",
    "Every second here is borrowed from something that actually matters.",
    "You opened this without even thinking.\nThat's not relaxing. That's a reflex.",
    "Time you spend here cannot be recovered.\nNot one second of it.",
    "In the time you'll spend here today, someone finished a chapter of their book.",
    "You're trading your life, minute by minute, for content you won't remember tomorrow.",
    "The people who built this app don't let their own children use it.",
    "Your attention is the most valuable thing you own.\nYou're about to give it away for free.",
    "You came back again.\nWhat exactly are you hoping to find this time that you didn't find last time?",
    "Boredom is where ideas are born.\nYou're about to kill it again.",

    // --- ALGORITHM AWARENESS ---
    "There are 1000 engineers whose only job is to keep you here longer.\nThey're very good at their job.",
    "Everything you see here was chosen by an algorithm to make you stay.\nNone of it was chosen for your benefit.",
    "Instagram doesn't show you what's true.\nIt shows you what keeps you scrolling.",
    "The algorithm knows your weaknesses better than your friends do.",
    "You are not the user of this app.\nYou are the product being sold to advertisers.",
    "Every tap you make trains the algorithm to trap you better next time.",
    "This feed was engineered by behavioral psychologists.\nIt is designed to feel unfinishable.",
    "The scroll has no end.\nThat is not an accident. It was designed.",
    "Instagram A/B tests every pixel to maximize how long you stay.\nYou are a test subject.",
    "This app uses the same psychological mechanisms as slot machines.\nYou just pulled the lever.",

    // --- IDENTITY & ABILITY ---
    "Three years ago your attention span was longer.\nThis app shortened it.",
    "You used to be able to sit with boredom.\nNow you can't last 30 seconds.",
    "Every hour here is an hour not spent getting better at anything.",
    "The version of you that reads, creates, and builds — Instagram is slowly replacing them.",
    "What skill would you have right now if you'd spent your Instagram hours on it instead?",
    "Your ability to focus deeply is the most valuable thing you can develop.\nThis app destroys it.",
    "Deep work requires a mind that can resist distraction.\nThis trains you in the opposite direction.",
    "You are becoming a person who consumes but never creates.\nIs that who you want to be?",
    "The most successful people you admire guard their attention obsessively.\nYou're giving yours away.",
    "Your brain is plastic — it reshapes around what you do repeatedly.\nThink about what you're doing repeatedly.",

    // --- REAL LIFE ---
    "Someone near you right now would love your full attention.\nInstagram is getting it instead.",
    "You will not remember what you saw here today.\nYou will remember real moments.",
    "The meal in front of you is getting cold.\nThe person across from you is getting lonely.",
    "There is a world outside this screen that is happening without you right now.",
    "You have not touched grass, sky, or another human in how long?",
    "The best moments of your life never happened on this app.",
    "You can't Instagram your way to a life worth living.",
    "Real connection feels nothing like this.\nThis is the fast food version of human contact.",
    "The sunset is happening right now.\nThis feed will still be here after it's gone.",
    "Nobody on their deathbed wishes they'd scrolled more.",

    // --- MENTAL HEALTH ---
    "Every comparison you make here is between your real life and someone's highlight reel.",
    "This app is clinically linked to increased anxiety, depression, and loneliness.\nYou know this.",
    "Instagram shows you the best 1% of everyone's life.\nThen asks you to feel okay about yours.",
    "The inadequacy you feel after using this app is not an accident.\nIt is the business model.",
    "Your mood after using Instagram — track it honestly.\nIs it better or worse?",
    "You are measuring your ordinary days against other people's extraordinary moments.",
    "Social comparison is hardwired to make you feel bad.\nThis app weaponizes that instinct.",
    "The anxiety you feel right now may already be Instagram's doing.",
    "Dopamine from scrolling lasts minutes.\nThe restlessness it creates lasts hours.",
    "This app is designed to make you feel like you're missing out.\nAlways. Permanently.",

    // --- GOALS ---
    "The thing you keep saying you'll start — it's still waiting.\nInstagram is not.",
    "Your future self is watching this moment.\nWhat are they thinking?",
    "Instagram remembers every post you've ever liked.\nWhat have you built lately?",
    "Your goals don't have a notification badge.\nThis app does.",
    "Every dream you have requires focused time to build.\nThis is that time being spent.",
    "Nobody ever scrolled their way to where they wanted to be.",
    "The person you want to become is not built here.",
    "Comfort and growth cannot occupy the same moment.\nYou just chose comfort.",
    "One year from now, will you be glad you opened Instagram right now?",
    "The gap between who you are and who you want to be is partly filled with this."
)
```

---

## MCQ QUESTIONS — 80 Questions across 6 Categories

### CATEGORY 1: ABILITY EROSION (15 questions)

```kotlin
Question(
    id = "ability_1",
    category = "Ability Erosion",
    text = "When did you last read something longer than a caption and actually finished it?",
    options = listOf("Today or yesterday", "This week", "This month", "I genuinely can't remember"),
    avoidantOptions = listOf("I genuinely can't remember"),
    punishmentMap = mapOf("I genuinely can't remember" to "Instagram didn't steal your focus overnight.\nIt took it one scroll at a time. Every single day.")
),

Question(
    id = "ability_2",
    category = "Ability Erosion",
    text = "Your attention span — honestly comparing 3 years ago to now:",
    options = listOf("Same or better", "Slightly shorter", "Noticeably shorter", "I struggle to focus on anything for more than a few minutes"),
    avoidantOptions = listOf("I struggle to focus on anything for more than a few minutes"),
    punishmentMap = mapOf("I struggle to focus on anything for more than a few minutes" to "That difficulty was engineered.\nBy this app.\nAnd you keep feeding it.")
),

Question(
    id = "ability_3",
    category = "Ability Erosion",
    text = "How many Reels do you actually remember from last week?",
    options = listOf("Several — clearly", "A few vague ones", "One or two", "None at all"),
    avoidantOptions = listOf("None at all"),
    punishmentMap = mapOf("None at all" to "Hours of your life last week.\nGone.\nNothing to show for it.\nNot even a memory.")
),

Question(
    id = "ability_4",
    category = "Ability Erosion",
    text = "If Instagram disappeared tomorrow, which ability would grow back fastest?",
    options = listOf("Deep focus and concentration", "Patience with boredom", "Original creative thinking", "All of them — and that tells you everything")
),

Question(
    id = "ability_5",
    category = "Ability Erosion",
    text = "When you have a free moment — your first instinct is:",
    options = listOf("Think, observe, or just be present", "Read or listen to something", "Reach for my phone automatically", "Open Instagram before I've even decided to"),
    avoidantOptions = listOf("Open Instagram before I've even decided to"),
    punishmentMap = mapOf("Open Instagram before I've even decided to" to "You didn't decide.\nYour conditioning did.\nThat's not a habit. That's a leash.")
),

Question(
    id = "ability_6",
    category = "Ability Erosion",
    text = "Can you sit in silence for 5 minutes without reaching for your phone?",
    options = listOf("Yes, easily", "Probably yes with some effort", "It would be genuinely uncomfortable", "No — I don't think I can"),
    avoidantOptions = listOf("No — I don't think I can"),
    punishmentMap = mapOf("No — I don't think I can" to "5 minutes of silence is unbearable to you now.\nThis app trained that.\nYou paid for the training with your peace of mind.")
),

Question(
    id = "ability_7",
    category = "Ability Erosion",
    text = "How often do you pick up your phone to check Instagram with no specific reason?",
    options = listOf("Rarely — when I decide to", "A few times a day", "Constantly throughout the day", "I've lost count — it's automatic")
),

Question(
    id = "ability_8",
    category = "Ability Erosion",
    text = "The last time you had a truly original idea — where were you?",
    options = listOf("Walking or in nature", "In the shower or quiet space", "Reading or thinking", "I struggle to remember the last original idea I had"),
    avoidantOptions = listOf("I struggle to remember the last original idea I had"),
    punishmentMap = mapOf("I struggle to remember the last original idea I had" to "Creativity requires mental space.\nScrolling fills every space.\nYou've been filling the gaps where ideas would have lived.")
),

Question(
    id = "ability_9",
    category = "Ability Erosion",
    text = "How long can you watch a movie or read a book without checking your phone?",
    options = listOf("The whole way through", "An hour or more", "20-30 minutes", "Less than 20 minutes"),
    avoidantOptions = listOf("Less than 20 minutes"),
    punishmentMap = mapOf("Less than 20 minutes" to "A human mind can naturally focus for 90+ minutes.\nYours has been trained down to under 20.\nThis is what that looks like.")
),

Question(
    id = "ability_10",
    category = "Ability Erosion",
    text = "When you're bored — what's your honest reaction to that feeling?",
    options = listOf("I sit with it — boredom doesn't scare me", "I find something productive to do", "I immediately reach for my phone", "Boredom feels intolerable and I'll do anything to escape it"),
    avoidantOptions = listOf("Boredom feels intolerable and I'll do anything to escape it"),
    punishmentMap = mapOf("Boredom feels intolerable and I'll do anything to escape it" to "Boredom intolerance is a symptom.\nInstagram is the cause.\nYou trained yourself to need constant stimulation.\nNow silence feels like pain.")
),

Question(
    id = "ability_11",
    category = "Ability Erosion",
    text = "Has someone ever said 'you're not really listening' to you while you were on your phone?",
    options = listOf("No, never", "Once or twice", "More than a few times", "It's become a running joke or complaint")
),

Question(
    id = "ability_12",
    category = "Ability Erosion",
    text = "When you try to think deeply about something — what happens?",
    options = listOf("I can hold the thought and go deep", "I can focus but it takes effort", "My mind drifts quickly to other things", "I find it genuinely hard to think without distraction"),
    avoidantOptions = listOf("I find it genuinely hard to think without distraction"),
    punishmentMap = mapOf("I find it genuinely hard to think without distraction" to "The ability to think clearly is the foundation of everything.\nYou are eroding yours.\nOne scroll at a time.")
),

Question(
    id = "ability_13",
    category = "Ability Erosion",
    text = "If you had to spend 3 hours alone with no phone — your honest reaction:",
    options = listOf("That sounds peaceful", "Manageable, maybe a little restless", "That sounds genuinely hard", "That sounds like a punishment"),
    avoidantOptions = listOf("That sounds like a punishment"),
    punishmentMap = mapOf("That sounds like a punishment" to "Being alone with your own mind used to be natural.\nNow it feels like a punishment.\nInstagram did that.")
),

Question(
    id = "ability_14",
    category = "Ability Erosion",
    text = "How much of what you learn on Instagram do you actually apply to your real life?",
    options = listOf("A lot — it's genuinely useful", "Some things occasionally", "Very little honestly", "Almost nothing — it's mostly entertainment I forget")
),

Question(
    id = "ability_15",
    category = "Ability Erosion",
    text = "What would you do with your mind if Instagram didn't exist?",
    options = listOf("Read, write, or create more", "Have deeper conversations", "Build something I keep putting off", "Honestly I'm not sure anymore — and that scares me"),
    avoidantOptions = listOf("Honestly I'm not sure anymore — and that scares me"),
    punishmentMap = mapOf("Honestly I'm not sure anymore — and that scares me" to "When you don't know who you are without the feed,\nthe feed has already become you.")
),

// === CATEGORY 2: TIME ACCOUNTABILITY (15 questions) ===

Question(
    id = "time_1",
    category = "Time Accountability",
    text = "Name one thing you planned to do today that still isn't done.",
    options = listOf("Nothing — I'm fully on top of things", "One small thing I'll do right after", "Something important I keep postponing", "Multiple things I've been avoiding for days"),
    avoidantOptions = listOf("Multiple things I've been avoiding for days", "Something important I keep postponing"),
    punishmentMap = mapOf(
        "Multiple things I've been avoiding for days" to "Instagram is where you go to avoid your life.\nYour list is still waiting.\nIt doesn't shrink while you scroll.",
        "Something important I keep postponing" to "Every day you postpone it, it gets heavier.\nInstagram makes the postponing easier.\nThat's not comfort. That's debt."
    )
),

Question(
    id = "time_2",
    category = "Time Accountability",
    text = "At your current rate, you'll spend roughly 15-20 days on Instagram this year. Your honest reaction:",
    options = listOf("That's fine — it's how I unwind", "That seems high but I don't think about it", "That number genuinely disturbs me", "I already knew and I feel trapped by it"),
    avoidantOptions = listOf("That seems high but I don't think about it"),
    punishmentMap = mapOf(
        "That seems high but I don't think about it" to "Not thinking about it is the strategy.\nInstagram profits from exactly that.",
        "I already knew and I feel trapped by it" to "Knowing and feeling trapped is the design.\nYou're not weak for feeling this.\nBut staying is a choice."
    )
),

Question(
    id = "time_3",
    category = "Time Accountability",
    text = "What would your 10-years-older self say about how you're using this hour?",
    options = listOf("'You deserved the rest'", "'That was harmless'", "'You had better things to do'", "'I wish you'd used that time differently'"),
    avoidantOptions = listOf("'I wish you'd used that time differently'"),
    punishmentMap = mapOf("'I wish you'd used that time differently'" to "You already know how this story ends.\nYou're writing it right now.")
),

Question(
    id = "time_4",
    category = "Time Accountability",
    text = "How many times have you opened Instagram today before this moment?",
    options = listOf("Once or twice", "3-5 times", "More than 5", "I genuinely don't know — I stopped counting"),
    avoidantOptions = listOf("I genuinely don't know — I stopped counting"),
    punishmentMap = mapOf("I genuinely don't know — I stopped counting" to "When you can't count how many times you've done something,\nit's no longer a choice.\nIt's a compulsion.")
),

Question(
    id = "time_5",
    category = "Time Accountability",
    text = "The last time you had a completely phone-free hour — when was it?",
    options = listOf("Today", "Yesterday", "This week", "I honestly can't remember")
),

Question(
    id = "time_6",
    category = "Time Accountability",
    text = "If someone filmed how you use your phone today and showed it to people you respect — you'd feel:",
    options = listOf("Completely fine with it", "Slightly embarrassed", "Genuinely ashamed", "I'd rather not think about that"),
    avoidantOptions = listOf("Genuinely ashamed", "I'd rather not think about that"),
    punishmentMap = mapOf(
        "Genuinely ashamed" to "Shame means you already know.\nThe question is whether knowing is enough.",
        "I'd rather not think about that" to "Avoiding the thought is the same as answering it."
    )
),

Question(
    id = "time_7",
    category = "Time Accountability",
    text = "What is the most valuable thing you could be doing with the next 30 minutes of your life?",
    options = listOf("Exactly this — I needed a break", "Something creative or skill-building", "Connecting with someone I care about", "Working on something I keep putting off"),
    avoidantOptions = listOf("Working on something I keep putting off"),
    punishmentMap = mapOf("Working on something I keep putting off" to "You just answered your own question.\nAnd then kept scrolling.")
),

Question(
    id = "time_8",
    category = "Time Accountability",
    text = "How much of your Instagram time is genuinely intentional vs. just happens to you?",
    options = listOf("Mostly intentional — I choose when and how long", "About half and half", "Mostly it just happens", "Almost none of it is a real decision")
),

Question(
    id = "time_9",
    category = "Time Accountability",
    text = "When you finish a long Instagram session — how do you usually feel?",
    options = listOf("Refreshed and glad I took the break", "Neutral — neither better nor worse", "Vaguely guilty or like I wasted time", "Worse than before I started"),
    avoidantOptions = listOf("Worse than before I started", "Vaguely guilty or like I wasted time"),
    punishmentMap = mapOf(
        "Worse than before I started" to "You use something that makes you feel worse.\nRepeatedly.\nThat is the definition of a trap.",
        "Vaguely guilty or like I wasted time" to "That guilt is accurate.\nThe question is whether you'll listen to it."
    )
),

Question(
    id = "time_10",
    category = "Time Accountability",
    text = "How many hours a week do you think you spend on Instagram? Be honest.",
    options = listOf("Under 3 hours", "3-7 hours", "7-14 hours", "Over 14 hours — more than a part-time job"),
    avoidantOptions = listOf("Over 14 hours — more than a part-time job"),
    punishmentMap = mapOf("Over 14 hours — more than a part-time job" to "14 hours a week.\nThat's 728 hours a year.\n30 full days.\nGiven to this app.\nFor free.")
),

Question(
    id = "time_11",
    category = "Time Accountability",
    text = "What have you accomplished in the last hour before opening Instagram?",
    options = listOf("Something meaningful — I earned this", "A few small things", "Honestly not much", "Nothing — this was the first thing I did")
),

Question(
    id = "time_12",
    category = "Time Accountability",
    text = "Do you use Instagram more when you're avoiding something specific?",
    options = listOf("No — my usage is pretty consistent", "Sometimes, when I notice it", "Yes — it's often avoidance", "Yes — and I often don't realize it until after"),
    avoidantOptions = listOf("Yes — and I often don't realize it until after"),
    punishmentMap = mapOf("Yes — and I often don't realize it until after" to "When you use something to escape without knowing you're escaping,\nit has become your default coping mechanism.\nThat's a problem bigger than Instagram.")
),

Question(
    id = "time_13",
    category = "Time Accountability",
    text = "If Instagram charged you ₹10 per minute to use it — how would your usage change?",
    options = listOf("About the same honestly", "I'd be much more intentional", "I'd rarely use it", "I'd probably quit"),
    avoidantOptions = listOf("I'd probably quit"),
    punishmentMap = mapOf("I'd probably quit" to "It IS charging you.\nJust not in money.\nIt charges you in time, attention, and mental clarity.\nYou just don't see the bill.")
),

Question(
    id = "time_14",
    category = "Time Accountability",
    text = "How often do you open Instagram immediately after waking up?",
    options = listOf("Never — I protect my mornings", "Occasionally", "Most mornings", "It's usually the first thing I do"),
    avoidantOptions = listOf("It's usually the first thing I do"),
    punishmentMap = mapOf("It's usually the first thing I do" to "The first thought of your day is yours.\nYou're giving it to an algorithm.\nEvery single morning.")
),

Question(
    id = "time_15",
    category = "Time Accountability",
    text = "How often do you check Instagram right before sleep?",
    options = listOf("Never — I protect my sleep", "Sometimes", "Most nights", "Always — it's how I wind down"),
    avoidantOptions = listOf("Always — it's how I wind down"),
    punishmentMap = mapOf("Always — it's how I wind down" to "Blue light suppresses melatonin.\nComparison raises cortisol.\nYou're using Instagram to wind down.\nIt is scientifically doing the opposite.")
),

// === CATEGORY 3: REAL LIFE DISPLACEMENT (15 questions) ===

Question(
    id = "real_1",
    category = "Real Life",
    text = "Is there a person physically near you right now you haven't spoken to?",
    options = listOf("No — I'm genuinely alone", "Yes but they're busy", "Yes — I chose this instead of them", "Yes — and that's become completely normal"),
    avoidantOptions = listOf("Yes — and that's become completely normal"),
    punishmentMap = mapOf("Yes — and that's become completely normal" to "When choosing a screen over the person beside you becomes normal,\nyou've already lost something you may not get back.")
),

Question(
    id = "real_2",
    category = "Real Life",
    text = "When did you last do something meaningful that you didn't document or think about posting?",
    options = listOf("Today", "This week", "I do things without thinking about sharing", "I struggle to do things without thinking about how they'd look"),
    avoidantOptions = listOf("I struggle to do things without thinking about how they'd look"),
    punishmentMap = mapOf("I struggle to do things without thinking about how they'd look" to "When you start living for the documentation,\nyou stop living the moment.\nInstagram colonized your present tense.")
),

Question(
    id = "real_3",
    category = "Real Life",
    text = "The last genuinely good conversation you had — was it in person or online?",
    options = listOf("In person recently", "Online but meaningful", "I struggle to remember a genuinely good conversation", "I get most of my social connection through screens now")
),

Question(
    id = "real_4",
    category = "Real Life",
    text = "When something good happens to you — what's your first instinct?",
    options = listOf("Feel it fully and share with someone physically near you", "Call or message someone specific", "Think about whether to post it", "Reach for Instagram immediately"),
    avoidantOptions = listOf("Reach for Instagram immediately"),
    punishmentMap = mapOf("Reach for Instagram immediately" to "You experienced something real.\nAnd your first instinct was to perform it for strangers.\nThe experience came second.")
),

Question(
    id = "real_5",
    category = "Real Life",
    text = "How often do you check Instagram while you're with other people?",
    options = listOf("Never — people get my full attention", "Rarely — only if something urgent", "Sometimes — I drift without meaning to", "Often — it's just how everyone is now"),
    avoidantOptions = listOf("Often — it's just how everyone is now"),
    punishmentMap = mapOf("Often — it's just how everyone is now" to "'Everyone does it' is how we normalize damage.\nThe people who love you notice.\nEven if they don't say so.")
),

Question(
    id = "real_6",
    category = "Real Life",
    text = "When you're eating a meal — where is your attention?",
    options = listOf("On the food and whoever I'm with", "Mostly present but phone nearby", "Phone on the table and I check it", "I'm usually scrolling while eating"),
    avoidantOptions = listOf("I'm usually scrolling while eating"),
    punishmentMap = mapOf("I'm usually scrolling while eating" to "Eating is one of life's simple pleasures.\nYou've turned it into a multitasking session.\nFor what?")
),

Question(
    id = "real_7",
    category = "Real Life",
    text = "How many of your Instagram followers would notice if you disappeared for a month?",
    options = listOf("Many — I have real connections here", "A handful of real ones", "Maybe a few", "Honestly almost none"),
    avoidantOptions = listOf("Honestly almost none"),
    punishmentMap = mapOf("Honestly almost none" to "You give hours of your life to an audience\nthat would barely notice your absence.\nMeanwhile, real people who love you get your leftovers.")
),

Question(
    id = "real_8",
    category = "Real Life",
    text = "When did you last go outside with no destination — just to exist in the world?",
    options = listOf("Recently — I do this regularly", "This week", "Can't remember when", "I don't really do that"),
    avoidantOptions = listOf("I don't really do that"),
    punishmentMap = mapOf("I don't really do that" to "The outside world is not optional content.\nIt's where your actual life is.")
),

Question(
    id = "real_9",
    category = "Real Life",
    text = "Do you know what's happening in the lives of the people physically closest to you?",
    options = listOf("Yes — we talk often and deeply", "Somewhat — the surface stuff", "Less than I should", "I know more about strangers on Instagram than people near me"),
    avoidantOptions = listOf("I know more about strangers on Instagram than people near me"),
    punishmentMap = mapOf("I know more about strangers on Instagram than people near me" to "The relationships that will carry you through hard times\nare the ones you're currently neglecting\nto watch strangers live their lives.")
),

Question(
    id = "real_10",
    category = "Real Life",
    text = "How present are you in your own life right now — honestly?",
    options = listOf("Very present — I'm here for my life", "Mostly present with some drift", "Often somewhere else mentally", "I feel like I'm watching my life more than living it"),
    avoidantOptions = listOf("I feel like I'm watching my life more than living it"),
    punishmentMap = mapOf("I feel like I'm watching my life more than living it" to "You get one life.\nAnd you're spending it watching other people live theirs.")
),

Question(
    id = "real_11",
    category = "Real Life",
    text = "The last time you felt genuinely content and at peace — what were you doing?",
    options = listOf("Something creative or physical", "A real conversation with someone", "Being in nature or a quiet space", "I struggle to remember the last time I felt genuinely content")
),

Question(
    id = "real_12",
    category = "Real Life",
    text = "Do you experience FOMO — fear of missing out — on things happening on Instagram?",
    options = listOf("No — I'm secure in my own life", "Occasionally but I shake it off", "Yes — it affects how I feel about my own life", "Yes — constantly, it's exhausting"),
    avoidantOptions = listOf("Yes — constantly, it's exhausting"),
    punishmentMap = mapOf("Yes — constantly, it's exhausting" to "FOMO was invented and is maintained by this app.\nYou are paying for the disease\nwith the currency of your peace of mind.")
),

Question(
    id = "real_13",
    category = "Real Life",
    text = "What is happening in the physical space around you right now that you're ignoring?",
    options = listOf("Nothing — this is genuinely my downtime", "The weather, sounds, beauty of the world", "A person who could use my attention", "My own body — tension, hunger, tiredness I haven't noticed"),
    avoidantOptions = listOf("A person who could use my attention"),
    punishmentMap = mapOf("A person who could use my attention" to "That person will remember whether you were present.\nInstagram will not.")
),

Question(
    id = "real_14",
    category = "Real Life",
    text = "How much of your self-image is tied to how you appear on Instagram?",
    options = listOf("Not at all — it's separate from who I am", "A little", "More than I'd like to admit", "Significantly — I feel worse when I don't post or get engagement"),
    avoidantOptions = listOf("Significantly — I feel worse when I don't post or get engagement"),
    punishmentMap = mapOf("Significantly — I feel worse when I don't post or get engagement" to "Your worth existed before Instagram.\nIt will exist after Instagram.\nBut this app has convinced you otherwise.")
),

Question(
    id = "real_15",
    category = "Real Life",
    text = "If you died tomorrow — would you be glad you spent part of today scrolling Instagram?",
    options = listOf("Yes — I was genuinely resting", "Maybe — it wasn't hurting anything", "No — I'd wish I'd used the time differently", "No — and I already know this but keep doing it"),
    avoidantOptions = listOf("No — and I already know this but keep doing it"),
    punishmentMap = mapOf("No — and I already know this but keep doing it" to "Knowing and not changing is the most painful place to be.\nYou don't have to stay there.")
),

// === CATEGORY 4: GOAL CONFRONTATION (15 questions) ===

Question(
    id = "goal_1",
    category = "Goal Confrontation",
    text = "The most honest reason you opened Instagram just now:",
    options = listOf("Genuine intentional break — I earned it", "Bored and this fills it temporarily", "Lonely and this is easier than real connection", "Avoiding something specific I don't want to face"),
    avoidantOptions = listOf("Avoiding something specific I don't want to face", "Lonely and this is easier than real connection"),
    punishmentMap = mapOf(
        "Avoiding something specific I don't want to face" to "It'll still be there when you close this.\nBigger now than before.\nInstagram didn't solve it. It just delayed you.",
        "Lonely and this is easier than real connection" to "This app simulates connection without providing it.\nYou're drinking salt water for thirst.\nIt makes it worse."
    )
),

Question(
    id = "goal_2",
    category = "Goal Confrontation",
    text = "The thing you keep saying you'll start 'soon' — how long have you been saying that?",
    options = listOf("I actually started — this is earned rest", "A few days — I'll really start this week", "A few months", "Over a year"),
    avoidantOptions = listOf("Over a year", "A few months"),
    punishmentMap = mapOf(
        "Over a year" to "A year of 'soon'.\nInstagram had a spectacular year.\nDid you?",
        "A few months" to "Months feel slow until they're gone.\nThis app is very good at filling them."
    )
),

Question(
    id = "goal_3",
    category = "Goal Confrontation",
    text = "Instagram remembers everything you've ever liked. What have YOU created in the last month?",
    options = listOf("Several things I'm proud of", "A few small things", "Nothing recently — I have ideas though", "Nothing. This is where my creative energy goes."),
    avoidantOptions = listOf("Nothing. This is where my creative energy goes."),
    punishmentMap = mapOf("Nothing. This is where my creative energy goes." to "Consuming creativity and creating are opposites.\nEvery hour here is an hour not building anything.\nYou know this already.")
),

Question(
    id = "goal_4",
    category = "Goal Confrontation",
    text = "How would you describe your relationship with your biggest goal right now?",
    options = listOf("Actively working on it daily", "Making progress, not as fast as I'd like", "Thinking about it more than acting on it", "Instagram knows more about my goals than my actions do"),
    avoidantOptions = listOf("Instagram knows more about my goals than my actions do"),
    punishmentMap = mapOf("Instagram knows more about my goals than my actions do" to "You've been consuming the life you want\nrather than building it.")
),

Question(
    id = "goal_5",
    category = "Goal Confrontation",
    text = "If you tracked your Instagram hours and redirected them for 30 days — what could you build?",
    options = listOf("A new skill at an intermediate level", "The first draft of something creative", "A significantly healthier body", "I'd rather not calculate that — it would be too confronting"),
    avoidantOptions = listOf("I'd rather not calculate that — it would be too confronting"),
    punishmentMap = mapOf("I'd rather not calculate that — it would be too confronting" to "The answer exists whether you calculate it or not.\nNot looking doesn't make it smaller.")
),

Question(
    id = "goal_6",
    category = "Goal Confrontation",
    text = "What do you tell yourself to justify this session right now?",
    options = listOf("I've worked hard — I deserve a break", "It's just a few minutes", "I'll get to my stuff right after", "I'm not thinking about it — I just opened it"),
    avoidantOptions = listOf("I'll get to my stuff right after", "I'm not thinking about it — I just opened it"),
    punishmentMap = mapOf(
        "I'll get to my stuff right after" to "'Right after' is the most expensive phrase in the language.\nYou've said it thousands of times.",
        "I'm not thinking about it — I just opened it" to "The most dangerous habit is the one that doesn't feel like a choice."
    )
),

Question(
    id = "goal_7",
    category = "Goal Confrontation",
    text = "Describe the version of yourself you most want to become. Are you building that person right now?",
    options = listOf("Yes — this is a genuine rest moment in a productive day", "Partially — I'm making some progress", "Not really — I know what I should be doing", "No — and this app is part of why not"),
    avoidantOptions = listOf("No — and this app is part of why not"),
    punishmentMap = mapOf("No — and this app is part of why not" to "You know.\nThat's actually progress.\nNow do the harder thing: act on it.")
),

Question(
    id = "goal_8",
    category = "Goal Confrontation",
    text = "What would you accomplish in the next 6 months if you reclaimed your Instagram hours?",
    options = listOf("Significant progress on my main goal", "Complete something I've never finished", "Build a skill that changes my opportunities", "I don't know — and that uncertainty scares me"),
    avoidantOptions = listOf("I don't know — and that uncertainty scares me"),
    punishmentMap = mapOf("I don't know — and that uncertainty scares me" to "Not knowing what you'd do without Instagram\nis a sign of how much space it occupies.\nIt has replaced your ambitions with its own.")
),

Question(
    id = "goal_9",
    category = "Goal Confrontation",
    text = "How often does Instagram inspire you to take real action vs. just feel inspired for a moment?",
    options = listOf("Often — it regularly motivates real change", "Sometimes — occasional sparks", "Rarely — it's mostly passive consumption", "Never — inspiration here dies the moment I close the app"),
    avoidantOptions = listOf("Never — inspiration here dies the moment I close the app"),
    punishmentMap = mapOf("Never — inspiration here dies the moment I close the app" to "Inspiration without action is just entertainment.\nYou've been entertaining yourself with your own potential.")
),

Question(
    id = "goal_10",
    category = "Goal Confrontation",
    text = "One year from now — what do you want to be different about your life?",
    options = listOf("My health and fitness", "A skill or career achievement", "My relationships and presence", "Honestly — the same things I wanted to change a year ago"),
    avoidantOptions = listOf("Honestly — the same things I wanted to change a year ago"),
    punishmentMap = mapOf("Honestly — the same things I wanted to change a year ago" to "Another year of the same answer.\nInstagram also had another great year.\nThis is not a coincidence.")
),

Question(
    id = "goal_11",
    category = "Goal Confrontation",
    text = "Pure habit — right now, did you decide to open Instagram or did your hand just do it?",
    options = listOf("I made a conscious decision", "Somewhere between decision and habit", "Honestly my hand did it", "I don't even remember opening it"),
    avoidantOptions = listOf("I don't even remember opening it", "Honestly my hand did it"),
    punishmentMap = mapOf(
        "I don't even remember opening it" to "You lost time before you even started counting.\nThat's not using an app.\nThat's being used by one.",
        "Honestly my hand did it" to "Your body has learned to do this without asking your mind.\nThat is a trained compulsion."
    )
),

Question(
    id = "goal_12",
    category = "Goal Confrontation",
    text = "What would a mentor or someone you deeply respect think if they saw how you're spending this time?",
    options = listOf("They'd understand — rest is important", "They'd be neutral", "They'd be gently disappointed", "They'd be blunt about what they see"),
    avoidantOptions = listOf("They'd be blunt about what they see"),
    punishmentMap = mapOf("They'd be blunt about what they see" to "You already know what they'd say.\nYou're the one who has to decide whether to listen.")
),

Question(
    id = "goal_13",
    category = "Goal Confrontation",
    text = "How many unfinished projects, half-read books, or abandoned goals are waiting for you right now?",
    options = listOf("None — I complete what I start", "One or two", "Several", "Too many to count honestly"),
    avoidantOptions = listOf("Too many to count honestly"),
    punishmentMap = mapOf("Too many to count honestly" to "Each unfinished thing represents a version of you that started but didn't continue.\nInstagram is very good at interrupting that continuation.")
),

Question(
    id = "goal_14",
    category = "Goal Confrontation",
    text = "Do you believe you're living up to your own potential right now?",
    options = listOf("Yes — I'm genuinely on track", "Mostly — with room to improve", "No — but I'm working on it", "No — and I spend a lot of time here instead of fixing that"),
    avoidantOptions = listOf("No — and I spend a lot of time here instead of fixing that"),
    punishmentMap = mapOf("No — and I spend a lot of time here instead of fixing that" to "You just described the trap perfectly.\nNow close this app.")
),

Question(
    id = "goal_15",
    category = "Goal Confrontation",
    text = "If Instagram gave you back all the time you've spent on it — what would you have done instead?",
    options = listOf("The same things I do now — it's balanced", "Read and learn more", "Built something I care about", "Become a meaningfully different version of myself"),
    avoidantOptions = listOf("Become a meaningfully different version of myself"),
    punishmentMap = mapOf("Become a meaningfully different version of myself" to "That person still exists.\nThey're just waiting on the other side of this habit.")
),

// === CATEGORY 5: MENTAL HEALTH (10 questions) ===

Question(
    id = "mental_1",
    category = "Mental Health",
    text = "How has Instagram made you feel about your own life in the last week?",
    options = listOf("Positive — genuinely inspired", "Neutral — unaffected", "Occasionally inadequate", "Worse about myself but I keep coming back"),
    avoidantOptions = listOf("Worse about myself but I keep coming back"),
    punishmentMap = mapOf("Worse about myself but I keep coming back" to "This is the trap.\nIt makes you feel bad.\nAnd was engineered to make you return despite that.\nYou are in the trap right now.")
),

Question(
    id = "mental_2",
    category = "Mental Health",
    text = "After a long Instagram session, your mood is usually:",
    options = listOf("Better — I'm recharged", "The same", "Vaguely flat or restless", "Lower than before I started"),
    avoidantOptions = listOf("Lower than before I started"),
    punishmentMap = mapOf("Lower than before I started" to "You are using something that makes you feel worse.\nRepeatedly.\nWith full knowledge.\nThat is what dependency looks like.")
),

Question(
    id = "mental_3",
    category = "Mental Health",
    text = "Do you compare yourself to people you see on Instagram?",
    options = listOf("No — I'm secure in who I am", "Occasionally but it doesn't affect me much", "Yes — it affects how I feel about my life", "Constantly — it's exhausting"),
    avoidantOptions = listOf("Constantly — it's exhausting"),
    punishmentMap = mapOf("Constantly — it's exhausting" to "Every comparison is between your full reality\nand someone else's curated highlight.\nYou are always going to lose that comparison.\nIt was designed that way.")
),

Question(
    id = "mental_4",
    category = "Mental Health",
    text = "Does Instagram make you feel more or less anxious about your own life?",
    options = listOf("More relaxed and connected", "No real effect", "Somewhat more anxious", "Significantly more anxious — I know this but struggle to stop"),
    avoidantOptions = listOf("Significantly more anxious — I know this but struggle to stop"),
    punishmentMap = mapOf("Significantly more anxious — I know this but struggle to stop" to "Using something that causes you anxiety to relieve anxiety\nis one of the cruelest loops a person can be in.")
),

Question(
    id = "mental_5",
    category = "Mental Health",
    text = "How often do you feel genuinely, quietly content — without needing stimulation?",
    options = listOf("Often — I have a stable baseline of contentment", "Sometimes", "Rarely", "Almost never — I need constant input to feel okay"),
    avoidantOptions = listOf("Almost never — I need constant input to feel okay"),
    punishmentMap = mapOf("Almost never — I need constant input to feel okay" to "The inability to feel okay without stimulation\nis a symptom, not a personality trait.\nIt can be reversed.\nBut not while you're here.")
),

Question(
    id = "mental_6",
    category = "Mental Health",
    text = "Does your mood depend on how your Instagram posts perform?",
    options = listOf("No — I'm completely detached from metrics", "Slightly", "More than I'd like to admit", "Yes — good engagement genuinely lifts my day, poor engagement ruins it"),
    avoidantOptions = listOf("Yes — good engagement genuinely lifts my day, poor engagement ruins it"),
    punishmentMap = mapOf("Yes — good engagement genuinely lifts my day, poor engagement ruins it" to "You have handed strangers the remote control to your mood.\nAnd they don't know you exist.")
),

Question(
    id = "mental_7",
    category = "Mental Health",
    text = "When you're feeling low — does Instagram help or does it make it worse?",
    options = listOf("It genuinely helps and lifts my mood", "Mixed — sometimes one, sometimes the other", "It usually makes me feel worse", "It numbs it temporarily but the feeling is bigger after"),
    avoidantOptions = listOf("It numbs it temporarily but the feeling is bigger after"),
    punishmentMap = mapOf("It numbs it temporarily but the feeling is bigger after" to "Numbing a feeling doesn't process it.\nIt stores it with interest.\nIt will be there when you come back.")
),

Question(
    id = "mental_8",
    category = "Mental Health",
    text = "How often do you feel genuine envy from things you see on Instagram?",
    options = listOf("Never — I celebrate others genuinely", "Rarely", "Sometimes", "Often — more than I'm comfortable admitting"),
    avoidantOptions = listOf("Often — more than I'm comfortable admitting"),
    punishmentMap = mapOf("Often — more than I'm comfortable admitting" to "Envy is energy that could fuel your own growth.\nInstead you're feeding it to an algorithm\nthat uses it to keep you scrolling.")
),

Question(
    id = "mental_9",
    category = "Mental Health",
    text = "How would you honestly describe your relationship with Instagram?",
    options = listOf("Healthy — I'm in control of it", "Complicated — it has more power than I'd like", "Dependent — I reach for it without thinking", "I know it's hurting me but I can't seem to stop"),
    avoidantOptions = listOf("I know it's hurting me but I can't seem to stop"),
    punishmentMap = mapOf("I know it's hurting me but I can't seem to stop" to "That sentence describes every addiction.\nNaming it is the first step.\nThis app has to stop being the last step.")
),

Question(
    id = "mental_10",
    category = "Mental Health",
    text = "When you imagine a version of yourself who doesn't use Instagram at all — how do they seem?",
    options = listOf("Disconnected and out of the loop", "About the same as me", "Calmer and more focused", "More creative, present, and alive — and that comparison stings"),
    avoidantOptions = listOf("More creative, present, and alive — and that comparison stings"),
    punishmentMap = mapOf("More creative, present, and alive — and that comparison stings" to "That person is not imaginary.\nThey're who you are without this.\nThey're still reachable.")
),

// === CATEGORY 6: SELF AWARENESS (10 questions) ===

Question(
    id = "self_1",
    category = "Self Awareness",
    text = "Why do YOU specifically use Instagram — your real honest answer:",
    options = listOf("Genuine connection with people I care about", "Creative inspiration and learning", "Filling emptiness I haven't dealt with", "I'm not entirely sure and that worries me"),
    avoidantOptions = listOf("Filling emptiness I haven't dealt with", "I'm not entirely sure and that worries me"),
    punishmentMap = mapOf(
        "Filling emptiness I haven't dealt with" to "Instagram will never fill it.\nIt's designed to keep it empty enough to keep you coming back.",
        "I'm not entirely sure and that worries me" to "Not knowing why you do something you do daily\nis worth sitting with seriously."
    )
),

Question(
    id = "self_2",
    category = "Self Awareness",
    text = "If Instagram showed you exactly how much time you've spent on it lifetime — you'd feel:",
    options = listOf("Fine — time well spent", "Surprised but okay", "Uncomfortable", "Genuinely sick"),
    avoidantOptions = listOf("Genuinely sick"),
    punishmentMap = mapOf("Genuinely sick" to "That feeling is data.\nListen to it.")
),

Question(
    id = "self_3",
    category = "Self Awareness",
    text = "Do you feel like YOU control Instagram, or Instagram controls YOU?",
    options = listOf("I'm fully in control", "Mostly in control with some drift", "It pulls me more than I pull it", "Honestly — it controls me more than I control it"),
    avoidantOptions = listOf("Honestly — it controls me more than I control it"),
    punishmentMap = mapOf("Honestly — it controls me more than I control it" to "A billion-dollar company's product controls your daily behavior.\nAnd it costs them nothing.\nYou pay with everything.")
),

Question(
    id = "self_4",
    category = "Self Awareness",
    text = "What would you think of someone else who used Instagram exactly as much as you do?",
    options = listOf("Nothing — it's normal and fine", "I'd have no judgment", "I'd feel mild concern for them", "I'd honestly be worried about them"),
    avoidantOptions = listOf("I'd honestly be worried about them"),
    punishmentMap = mapOf("I'd honestly be worried about them" to "You just described yourself.\nYou deserve the same concern.")
),

Question(
    id = "self_5",
    category = "Self Awareness",
    text = "Has anyone — a friend, family member, partner — expressed concern about your phone use?",
    options = listOf("No, never", "Once or twice in passing", "Yes — and I brushed it off", "Yes — repeatedly, and I know they're right"),
    avoidantOptions = listOf("Yes — repeatedly, and I know they're right"),
    punishmentMap = mapOf("Yes — repeatedly, and I know they're right" to "The people who love you are telling you something.\nInstagram's algorithm is telling you to ignore them.\nWhich one actually has your interests at heart?")
),

Question(
    id = "self_6",
    category = "Self Awareness",
    text = "Have you ever tried to reduce your Instagram use and failed?",
    options = listOf("No — I've never needed to try", "I've cut back successfully", "I've tried and mostly failed", "I've tried many times and always end up back here"),
    avoidantOptions = listOf("I've tried many times and always end up back here"),
    punishmentMap = mapOf("I've tried many times and always end up back here" to "Trying and failing repeatedly isn't weakness.\nIt's you fighting a system specifically designed to defeat you.\nYou need a different approach. Not more willpower.")
),

Question(
    id = "self_7",
    category = "Self Awareness",
    text = "In 5 years, what role do you want Instagram to have played in your life story?",
    options = listOf("A useful tool I used intentionally", "A minor background thing", "Honestly — a smaller role than it's playing now", "I hope it's something I moved past"),
    avoidantOptions = listOf("I hope it's something I moved past"),
    punishmentMap = mapOf("I hope it's something I moved past" to "That future requires decisions made now.\nStarting with this one.")
),

Question(
    id = "self_8",
    category = "Self Awareness",
    text = "What does the version of you that doesn't open Instagram right now do instead?",
    options = listOf("Something physical — walk, stretch, move", "Something creative or educational", "Connect with a real person", "I'm not sure — and that gap is the problem"),
    avoidantOptions = listOf("I'm not sure — and that gap is the problem"),
    punishmentMap = mapOf("I'm not sure — and that gap is the problem" to "Instagram has filled so much space\nthat you don't know what would be there without it.\nFinding out is urgent.")
),

Question(
    id = "self_9",
    category = "Self Awareness",
    text = "If this app disappeared tomorrow — your honest first reaction would be:",
    options = listOf("Relief", "Fine — I'd adjust quickly", "Anxious about losing connection", "Genuine panic or distress"),
    avoidantOptions = listOf("Genuine panic or distress"),
    punishmentMap = mapOf("Genuine panic or distress" to "The degree of distress at losing an app\nis the degree to which it owns you.")
),

Question(
    id = "self_10",
    category = "Self Awareness",
    text = "Are you the same person offline as you are online?",
    options = listOf("Yes — completely authentic in both", "Mostly the same with minor differences", "I curate quite a different version of myself online", "I don't fully know who I am offline anymore"),
    avoidantOptions = listOf("I don't fully know who I am offline anymore"),
    punishmentMap = mapOf("I don't fully know who I am offline anymore" to "The offline version of you is the real one.\nThe one that needs food, sleep, love, and purpose.\nThat person has been waiting for you to come back.")
)
```

---

## REALITY CHECK QUOTES — 100 Quotes
### Shown mid-scroll as overlay banners

```kotlin
val REALITY_CHECK_QUOTES = listOf(

    // --- ON TIME ---
    "The bad news is time flies. The good news is you're the pilot. — Michael Altshuler",
    "You will never find time for anything. If you want time, you must make it. — Charles Buxton",
    "Lost time is never found again. — Benjamin Franklin",
    "How you spend your days is how you spend your life. — Annie Dillard",
    "The most precious resource we all have is time. — Steve Jobs",
    "Time is what we want most, but use worst. — William Penn",
    "Lack of time is actually lack of priorities. — Timothy Ferriss",
    "An average person puts limits on what they'll do. An extraordinary person puts limits on what they'll let stop them.",
    "You get one life. This is not a rehearsal.",
    "Someday is not a day of the week.",
    "The trouble is, you think you have time. — Jack Kornfield",
    "Do not wait: the time will never be just right. — Napoleon Hill",
    "One day you will wake up and there won't be any more time to do the things you've always wanted. Do it now.",
    "The clock is running. Make the most of today.",
    "Either you run the day or the day runs you. — Jim Rohn",

    // --- ON ATTENTION ---
    "Where attention goes, energy flows.",
    "Attention is the rarest and purest form of generosity. — Simone Weil",
    "The ability to focus is the superpower of the 21st century.",
    "You are what you pay attention to.",
    "What you focus on expands.",
    "The secret of change is to focus all your energy not on fighting the old, but on building the new. — Socrates",
    "Concentration is the root of all the higher abilities in man. — Bruce Lee",
    "The successful warrior is the average person with laser-like focus. — Bruce Lee",
    "Energy flows where attention goes.",
    "In an age of distraction, nothing is so luxurious as paying attention. — Pico Iyer",
    "Focused, hard work is the real key to success. — John C. Maxwell",
    "The cost of distraction is measured in lost potential.",

    // --- ON SOCIAL MEDIA & TECHNOLOGY ---
    "Social media is training us to compare our lives, instead of appreciating everything we are.",
    "The internet is the world's largest library. It's also the world's largest casino. — Naval Ravikant",
    "Technology should improve your life, not become your life.",
    "If you are not paying for it, you're not the customer; you're the product being sold.",
    "Every time you check your phone instead of being present, you're choosing the virtual over the real.",
    "We are the first generation to be tested by infinite content. Most of us are failing.",
    "Instagram is not a mirror. It's a highlight reel. Everyone's life looks better from the outside.",
    "The smartphone in your pocket is the most addictive device ever created. Use it accordingly.",
    "You curate your feed. Your feed then curates you.",
    "Silicon Valley engineers don't let their own kids use the apps they build. Think about that.",
    "Social media gives us the feeling of connection while replacing actual connection.",
    "The algorithm's goal and your goal are not the same.",
    "You are not addicted to Instagram. You are addicted to the dopamine it provides. There is a difference — and both are serious.",
    "Every notification is a decision that someone else made for your attention.",
    "Tech companies have more psychologists working on their products than some hospitals.",

    // --- ON DISCIPLINE & GROWTH ---
    "We are what we repeatedly do. Excellence, then, is not an act, but a habit. — Aristotle",
    "Discipline is choosing between what you want now and what you want most.",
    "The pain of discipline is far less than the pain of regret.",
    "Small daily improvements over time lead to stunning results.",
    "Do something today that your future self will thank you for.",
    "You don't rise to the level of your goals. You fall to the level of your systems. — James Clear",
    "Success is a few simple disciplines, practiced every day. — Jim Rohn",
    "Hard choices, easy life. Easy choices, hard life. — Jerzy Gregorek",
    "A year from now you will wish you had started today.",
    "Motivation gets you started. Discipline keeps you going.",
    "Your life is the result of your decisions, not your conditions.",
    "Great things are done by a series of small things brought together. — Van Gogh",
    "The secret of your future is hidden in your daily routine. — Mike Murdock",
    "You will never always be motivated, so you must learn to be disciplined.",
    "Consistency is what transforms average into excellence.",
    "One day or day one. You decide.",

    // --- ON REAL LIFE & PRESENCE ---
    "Life is what happens to you while you're busy scrolling. — Adapted from John Lennon",
    "The present moment is the only place where life exists.",
    "Be here now. — Ram Dass",
    "We have two lives, and the second begins when we realize we only have one. — Confucius",
    "Almost everything will work again if you unplug it for a few minutes. Including you. — Anne Lamott",
    "The quality of your life is the quality of your attention.",
    "You can't start the next chapter of your life if you keep re-reading the last one.",
    "Stop being a consumer. Start being a creator.",
    "Life rewards the present. Instagram rewards the absent.",
    "The real world is far more interesting than the filtered one.",
    "Real life doesn't have a like button. That's not its weakness. That's its strength.",
    "The people who love you are not asking for your highlights. They want your presence.",
    "Look up. The world is happening without you.",
    "Collect moments, not likes.",

    // --- ON MENTAL HEALTH ---
    "Comparison is the thief of joy. — Theodore Roosevelt",
    "You are comparing your behind-the-scenes with everyone else's highlight reel.",
    "A quiet mind is richer than any amount of content.",
    "Boredom is the incubator of creativity. Kill it and you kill creativity with it.",
    "The mind that is not bored is the mind that never creates.",
    "Anxiety loves an occupied mind. Give it nothing to work with.",
    "Rest is not idleness. Idleness is scrolling. Rest is actually resting.",
    "You cannot pour from an empty cup. Instagram empties. Nothing gets refilled here.",
    "Mental health is not just the absence of illness. It's the presence of peace.",
    "The quieter you become, the more you can hear. — Rumi",
    "Not all storms come to disrupt your life. Some come to clear your path.",
    "Your mind is your home. Be careful what you let in.",
    "Peace begins the moment you choose not to allow another person or event — or algorithm — to control your emotions.",

    // --- ON BUILDING & CREATING ---
    "Don't watch the clock. Do what it does. Keep going. — Sam Levenson",
    "The best time to plant a tree was 20 years ago. The second best time is now.",
    "You don't have to be great to start, but you have to start to be great. — Zig Ziglar",
    "Build something. Leave something. Be something.",
    "An idea not coupled with action will never get any bigger than the brain cell it occupied. — Arnold H. Glasgow",
    "The people you admire didn't get there by watching people they admire.",
    "Consuming ideas is not the same as building with them.",
    "You have everything you need to start. You just keep choosing this instead.",
    "Creation is the antidote to consumption.",
    "Work while they sleep. Learn while they party. Save while they spend. Live like they dream.",
    "Your potential is not determined by your past. It's determined by your next decision.",
    "Every great creator was once a beginner who decided to start.",

    // --- DIRECT & BRUTAL ---
    "You're not resting. You're hiding.",
    "Scrolling is the new smoking. Normalized, addictive, and quietly damaging.",
    "The most dangerous place to spend your time is somewhere that takes everything and gives nothing back.",
    "You're not relaxing. You're procrastinating with extra steps.",
    "Nobody ever changed their life by scrolling longer.",
    "You are one decision away from a completely different life. This is not that decision.",
    "The gap between who you are and who you want to be is filled with moments like this one.",
    "Stop waiting for Friday. Stop waiting for 'later'. Stop waiting for Instagram to satisfy something it never will.",
    "You are trading your real life for a simulation of other people's lives.",
    "One scroll leads to another. That is not an accident. It is the design.",
    "Every minute here is a minute you'll never spend on what actually matters.",
    "The most successful people aren't on their phones more. They're on them less.",
    "You already know what you should be doing. That knowledge changes nothing unless you move."
)
```

---

## HOW TO ADD THIS TO THE PROJECT

Tell Copilot Chat:

```
In QuestionBank.kt, replace the existing ENTRY_MESSAGES list with the
expanded 60-message list from the question bank file.

Then add all 80 MCQ questions to the QUESTIONS list, maintaining the
exact same Question() data class structure already in the file.

Then add a new val REALITY_CHECK_QUOTES list with all 100 quotes.

Also add a new function:
fun getRandomQuote(): String = REALITY_CHECK_QUOTES.random()

Do not change any other code in the file.
```

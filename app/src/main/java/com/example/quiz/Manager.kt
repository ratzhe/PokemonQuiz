package com.example.quiz

object Manager {

    var user = ""

    var curPoints = 0

    var leaderboard = ArrayList<UserModel>()

    var desc:List<UserModel> = listOf()

    var randomized:List<String> = listOf()


    fun addUserLeaderboard(user:String, score:Int){
        val userModel = UserModel(user, score)
        leaderboard.add(userModel)
        desc = leaderboard.sortedWith(compareByDescending({ it.points }))
    }

    fun getQuestions(): List<Question>{
        val questionsList = ArrayList<Question>()

        val q1 = Question(1, "Que pokemon é este?",
            R.drawable.bulbasaur,
            "Bulbasaur",
            "Charmander",
            "Squirtle",
            "Pikachu",
            "Bulbasaur")

        questionsList.add(q1)

        val q2 = Question(2, "Que pokemon é este?",
            R.drawable.umbreon,
            "Umbreon",
            "Vaporeon",
            "Jolteon",
            "Flareon",
            "Umbreon")

        questionsList.add(q2)

        val q3 = Question(3, "Que pokemon é este?",
            R.drawable.kartana,
            "Kartana",
            "Palossand",
            "Mimikyu",
            "Calesteela",
            "Kartana")

        questionsList.add(q3)

        val q4 = Question(4, "Que pokemon é este?",
            R.drawable.ekans,
            "Ekans",
            "Arbok",
            "Seviper",
            "Silicobra",
            "Ekans")

        questionsList.add(q4)

        val q5 = Question(5, "Que pokemon é este?",
            R.drawable.growlithe,
            "Growlithe",
            "Arcanine",
            "Eevee",
            "Houndour",
            "Growlithe")

        questionsList.add(q5)


        val q6 = Question(6, "Que pokemon é este?",
            R.drawable.slugma,
            "Slugma",
            "Magmar",
            "Magcargo",
            "Typhlosion",
            "Slugma")

        questionsList.add(q6)

        val q7 = Question(7, "Que pokemon é este?",
            R.drawable.pikachu,
            "Pikachu",
            "Pichu",
            "Raichu",
            "Ratata",
            "Pikachu")

        questionsList.add(q7)


        return questionsList.toList().shuffled()
    }

}
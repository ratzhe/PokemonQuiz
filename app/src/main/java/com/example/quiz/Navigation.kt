package com.example.quiz

import androidx.compose.material3.Text

import android.graphics.Color.parseColor
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.room.Room
import com.example.quiz.room.AppDatabase
import com.example.quiz.room.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

val String.color
    get() = Color(parseColor(this))

val gameFont = FontFamily(Font(R.font.pressstart))

val colorRed = "#ee1b24".color
val colorBlue = "#299cc8".color
val colorYellow = "#ffd922".color

@Composable
fun Navigation(){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route){
        composable(route = Screen.MainScreen.route){

            MainScreen(navController = navController)
        }
        composable(
            route = Screen.Question.route,
            arguments = listOf(
                navArgument("name"){
                    type = NavType.StringType

                }
            )
        ){
            Question(navController = navController)
        }

        composable(
            route = Screen.Leaderboard.route
        ){
            Leaderboard()
        }
    }
}

var playMenu = false

@OptIn(ExperimentalTextApi::class)
@Composable
fun MainScreen(navController: NavController) {

    val context = LocalContext.current
    mQuestionsList = Manager.getQuestions()
    curPos = 1

    val transition = rememberInfiniteTransition()
    val animatedRed by transition.animateColor(
        initialValue = colorRed,
        targetValue = colorRed.copy(alpha = 0.9f),
        animationSpec = infiniteRepeatable(
            animation = tween(80),
            repeatMode = RepeatMode.Reverse
        )
    )

    val animatedBlue by transition.animateColor(
        initialValue = colorBlue,
        targetValue = colorBlue.copy(alpha = 0.9f),
        animationSpec = infiniteRepeatable(
            animation = tween(80),
            repeatMode = RepeatMode.Reverse
        )
    )

    val nameState = remember { mutableStateOf("") }

    var getLeadBool = true

    val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "user"
    ).build()

    Manager.leaderboard.clear()

    var getUsers:List<User> = listOf()

    LaunchedEffect(getLeadBool) {

        CoroutineScope(Dispatchers.IO).launch {
            if (db.userDao().getAll().isNotEmpty()) {
                getUsers = db.userDao().getAll()

                for (i in getUsers) {
                    Manager.addUserLeaderboard(i.user, i.points)
                }

                getLeadBool = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

            Box() {

                val offset = -30.dp

                val context = LocalContext.current

                Text(
                    text = "Pokemon Quiz",
                    color = animatedRed,
                    fontFamily = gameFont,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                    style = TextStyle.Default.copy(
                        shadow =  Shadow(
                            color =  colorRed,
                            offset = Offset(2f, 2f),
                            blurRadius = 30f
                        ),
                    ),
                    modifier = Modifier
                        .padding(top = 50.dp, bottom = 100.dp)
                )
            }

            Card(
                backgroundColor = "#000000".color,
                modifier = Modifier
                    .drawBehind {
                        drawIntoCanvas {
                            val paint = Paint()
                            val frameworkPaint = paint.asFrameworkPaint()

                            it.drawRoundRect(
                                0f,
                                0f,
                                this.size.width,
                                this.size.height,
                                0.dp.toPx(),
                                0.dp.toPx(),
                                paint
                            )
                        }
                    }
                    .clip(RoundedCornerShape(18.dp))
                    .background("#CC3CFF".color)
                    .size(310.dp, 190.dp)



            ){

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {

                    Text(
                        text = "DIGITE SEU NOME",
                        color = colorRed,
                        fontSize = 18.sp,
                        fontFamily = gameFont,
                        textAlign = TextAlign.Center,
                        modifier = Modifier

                            .padding(top = 25.dp)

                    )

                    TextField(
                        textStyle = LocalTextStyle.current.copy(color = "#ee1b24".color),
                        value = nameState.value,
                        onValueChange = {
                            nameState.value = it},
                        label = { Text(text = stringResource(id = R.string.name))},

                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = "#ee1b24".color,
                            unfocusedBorderColor = "#ee1b24".color,
                            focusedLabelColor = "#ee1b24".color,
                            cursorColor = "#ee1b24".color),

                        modifier = Modifier
                            .padding(top = 20.dp)
                            .width(270.dp)
                            .height(58.dp)

                    )
                }
            }
        }

        val shadowColorBlue = colorBlue.copy(alpha = 0.5f).toArgb()
        val animatedBlue = colorBlue.copy(alpha= 0f).toArgb()
        val offsetY = -150.dp

        Button(


            border = BorderStroke(4.dp,"#ee1b24".color),
            colors = ButtonDefaults.buttonColors(backgroundColor = "#000000".color),
            onClick = {onAddTapped(nameState.value, navController)},
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = offsetY)
                .drawBehind {
                    drawIntoCanvas {
                        val paint = Paint()
                        val frameworkPaint = paint.asFrameworkPaint()
                        frameworkPaint.color = shadowColorBlue

                        it.drawRoundRect(
                            0f,
                            0f,
                            this.size.width,
                            this.size.height,
                            0.dp.toPx(),
                            0.dp.toPx(),
                            paint
                        )

                    }

                }
                .background(Color.Black)
                .width(200.dp)
                .height(55.dp)


        ){
            Text(
                text = stringResource(id = R.string.cont),
                color = "#ee1b24".color

                )
        }

        val offsetY2 = -70.dp

        Button(


            border = BorderStroke(4.dp,"#ee1b24".color),
            colors = ButtonDefaults.buttonColors(backgroundColor = "#000000".color),
            onClick = { gotToLeader(navController) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = offsetY2)
                .drawBehind {
                    drawIntoCanvas {
                        val paint = Paint()

                        it.drawRoundRect(
                            0f,
                            0f,
                            this.size.width,
                            this.size.height,
                            0.dp.toPx(),
                            0.dp.toPx(),
                            paint
                        )

                    }

                }
                .background(Color.Black)
                .width(150.dp)
                .height(40.dp)



        ){
            Text(
                text = "Leaderboard",
                color = "#ee1b24".color,
                style = TextStyle.Default.copy(
                    drawStyle = Stroke(
                        miter = 5f,
                        width = 5f,
                    )
                ),
            )
        }
    }
}

private fun onAddTapped(nameState:String, navController: NavController){

    if(nameState.isNotEmpty()) {
        Manager.user = nameState
        navController.navigate(Screen.Question.route)
    }
}

private fun gotToLeader(navController: NavController){
    navController.navigate(Screen.Leaderboard.route)
}

var mQuestionsList: List<Question>? = null



private fun randomizeOptions(question: Question?):List<String>{
    val options = ArrayList<String>()
    options.add(0, question!!.optionOne)
    options.add(1, question!!.optionTwo)
    options.add(2, question!!.optionThree)
    options.add(3, question!!.optionFour)

    val randomized = options.shuffled()

    return randomized
}

private fun checkAnswer(answer:String, question:Question):Boolean{


    return answer == (question.correctAnswer)

}

private fun checkAnswer2(answer:String, question:Question, colorPos:Int, randomized:List<String>, questionColors: ArrayList<Color>){

    var getCorrectRandom = randomized.indexOf(question.correctAnswer)

    Log.d("TAG", questionColors.joinToString())

    if(answer == (question.correctAnswer)){
        questionColors[colorPos] = "#83FFC3".color

    }
    else{
        questionColors[colorPos] = "#EA3838".color
        questionColors[getCorrectRandom] = "#83FFC3".color

    }

    Log.d("TAG2", questionColors.joinToString())

}

var curPos = 1
var score = 0


@Composable
fun Question(navController: NavController) {

    val context = LocalContext.current

    val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "user"
    ).build()


    var countPoints by remember { mutableStateOf(true) }

    var tickPoints by remember { mutableStateOf(30) }


    var count by remember { mutableStateOf(false) }

    var ticks by remember { mutableStateOf(0) }


    var shadowColor1 by remember {mutableStateOf(colorRed.copy(alpha = 0.5f).toArgb())}
    var transparent1 by remember {mutableStateOf(colorRed.copy(alpha= 0f).toArgb())}

    var shadowColor2 by remember {mutableStateOf(colorRed.copy(alpha = 0.5f).toArgb())}
    var transparent2 by remember {mutableStateOf(colorRed.copy(alpha= 0f).toArgb())}

    var shadowColor3 by remember {mutableStateOf(colorRed.copy(alpha = 0.5f).toArgb())}
    var transparent3 by remember {mutableStateOf(colorRed.copy(alpha= 0f).toArgb())}

    var shadowColor4 by remember {mutableStateOf(colorRed.copy(alpha = 0.5f).toArgb())}
    var transparent4 by remember {mutableStateOf(colorRed.copy(alpha= 0f).toArgb())}

    var color1 by remember {mutableStateOf("#ffd922".color)}
    var color2 by remember {mutableStateOf("#ffd922".color)}
    var color3 by remember {mutableStateOf("#ffd922".color)}
    var color4 by remember {mutableStateOf("#ffd922".color)}


    var questionColors by remember {mutableStateOf(ArrayList<Color>())}

    questionColors.add(0, color1)
    questionColors.add(1, color2)
    questionColors.add(2, color3)
    questionColors.add(3, color4)


    LaunchedEffect(tickPoints, countPoints) {
        if(tickPoints > 0 && countPoints) {
            delay(1.seconds)
            tickPoints--
            Log.d("TAGPoints", tickPoints.toString())
        }
        else if(tickPoints <= 0){
            countPoints = false
        }
    }


    val question: Question? = mQuestionsList!!.get(curPos - 1)

    var save = true

    if(Manager.randomized.isEmpty()){
        Manager.randomized = randomizeOptions(question)
    }

    val randomized = Manager.randomized

    var timeRemaining = remember { mutableStateOf(60) }

    if(curPos >= 10){
        Manager.curPoints = score

        LaunchedEffect(save) {
            CoroutineScope(Dispatchers.IO).launch {
                db.userDao().insert(User(Manager.user, Manager.curPoints))
                save = false
            }
        }
        Manager.randomized = listOf()
        curPos = 1
        score = 0
        navController.navigate(Screen.MainScreen.route)
    }

    LaunchedEffect(ticks, count) {
        if(ticks < 2 && count) {
            delay(1.seconds)
            ticks++
            Log.d("TAG3", ticks.toString())
        }
        else if(ticks >= 1){
            Log.d("TAGGOFASSE", questionColors[0].toString())



            count = false
            ticks = 0
            navController.navigate(Screen.Question.route)


        }

        if(ticks == 1){

            questionColors[0] = "#ffd922".color
            questionColors[1] = "#ffd922".color
            questionColors[2] = "#ffd922".color
            questionColors[3] = "#ffd922".color

            curPos +=1
            Manager.randomized = listOf()

            Log.d("TAG OPTIONS", Manager.randomized.toString())


        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        androidx.compose.material.Text(
            text = "Pontuação: " + score.toString(),
            color = Color.White,
            fontSize = 15.sp,
            fontFamily = gameFont,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .width(350.dp)
                .padding(top = 10.dp, start = 5.dp)

        )

        androidx.compose.material.Text(
            text = tickPoints.toString(),
            color = Color.White,
            fontSize = 15.sp,
            fontFamily = gameFont,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .width(350.dp)
                .padding(top = 10.dp, end = 0.dp)

        )

        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {


            Card(
                backgroundColor = "#000000".color,
                modifier = Modifier
                    .padding(top = 35.dp)
                    .drawBehind {
                        drawIntoCanvas {
                            val paint = Paint()
                            val frameworkPaint = paint.asFrameworkPaint()


                            it.drawRoundRect(
                                0f,
                                0f,
                                this.size.width,
                                this.size.height,
                                0.dp.toPx(),
                                0.dp.toPx(),
                                paint
                            )
                        }
                    }
                    .size(300.dp, 200.dp)




            ){
                Image(
                    painter = painterResource(question!!.image),
                    contentDescription = "imagem",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier

                )
            }

            androidx.compose.material.Text(
                text = question!!.question,
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = gameFont,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(350.dp)
                    .padding(top = 35.dp)

            )

            val shadowColorBlue = colorBlue.copy(alpha = 0.5f).toArgb()
            val transparentBlue = colorBlue.copy(alpha= 0f).toArgb()
            val offsetY = 40.dp
            val padBottom = 35.dp


            Button(


                border = BorderStroke(4.dp,color1),
                colors = ButtonDefaults.buttonColors(backgroundColor = "#000000".color),
                onClick = {
                    if(count == false) {
                        checkAnswer2(randomized[0], question, 0, randomized, questionColors)
                        color1 = questionColors[0]

                        count = true

                        if(checkAnswer(randomized[0], question)){
                            score += (100 * (tickPoints / 2))
                        }

                    }
                },
                modifier = Modifier
                    .offset(y = offsetY)
                    .padding(bottom = padBottom)
                    .drawBehind {
                        drawIntoCanvas {
                            val paint = Paint()
                            val frameworkPaint = paint.asFrameworkPaint()

                            it.drawRoundRect(
                                0f,
                                0f,
                                this.size.width,
                                this.size.height,
                                0.dp.toPx(),
                                0.dp.toPx(),
                                paint
                            )

                        }

                    }
                    .background(Color.Black)
                    .width(340.dp)
                    .height(53.dp)


            ){
                Text(
                    text = randomized[0],
                    color = Color.White,

                    )
            }


            Button(


                border = BorderStroke(4.dp,color2),
                colors = ButtonDefaults.buttonColors(backgroundColor = "#000000".color),
                onClick = {
                    if(count == false) {
                        checkAnswer2(randomized[1], question, 1, randomized, questionColors)
                        color2 = questionColors[1]

                        count = true

                        if(checkAnswer(randomized[1], question)){
                            score += (100 * (tickPoints / 2))
                        }
                    }
                },
                modifier = Modifier
                    .offset(y = offsetY)
                    .padding(bottom = padBottom)
                    .drawBehind {
                        drawIntoCanvas {
                            val paint = Paint()
                            val frameworkPaint = paint.asFrameworkPaint()

                            it.drawRoundRect(
                                0f,
                                0f,
                                this.size.width,
                                this.size.height,
                                0.dp.toPx(),
                                0.dp.toPx(),
                                paint
                            )

                        }

                    }
                    .background(Color.Black)
                    .width(340.dp)
                    .height(53.dp)


            ){
                Text(
                    text = randomized[1],
                    color = Color.White,

                    )
            }


            Button(


                border = BorderStroke(4.dp,color3),
                colors = ButtonDefaults.buttonColors(backgroundColor = "#000000".color),
                onClick = {
                    if(count == false) {
                        checkAnswer2(randomized[2], question, 2, randomized, questionColors)
                        color3 = questionColors[2]

                        count = true

                        if(checkAnswer(randomized[2], question)){
                            score += (100 * (tickPoints / 2))
                        }
                    }
                },
                modifier = Modifier
                    .offset(y = offsetY)
                    .padding(bottom = padBottom)
                    .drawBehind {
                        drawIntoCanvas {
                            val paint = Paint()
                            val frameworkPaint = paint.asFrameworkPaint()
                            frameworkPaint.color = transparent3

                            it.drawRoundRect(
                                0f,
                                0f,
                                this.size.width,
                                this.size.height,
                                0.dp.toPx(),
                                0.dp.toPx(),
                                paint
                            )

                        }

                    }
                    .background(Color.Black)
                    .width(340.dp)
                    .height(53.dp)


            ){
                Text(
                    text = randomized[2],
                    color = Color.White,

                    )
            }


            Button(


                border = BorderStroke(4.dp,color4),
                colors = ButtonDefaults.buttonColors(backgroundColor = "#000000".color),
                onClick = {
                    if(count == false) {
                        checkAnswer2(randomized[3], question, 3, randomized, questionColors)
                        color4 = questionColors[3]

                        count = true

                        if(checkAnswer(randomized[3], question)){
                            score += (100 * (tickPoints / 2))
                        }
                    }
                },
                modifier = Modifier
                    .offset(y = offsetY)
                    .padding(bottom = padBottom)
                    .drawBehind {
                        drawIntoCanvas {
                            val paint = Paint()
                            val frameworkPaint = paint.asFrameworkPaint()
                            frameworkPaint.color = transparent4

                            it.drawRoundRect(
                                0f,
                                0f,
                                this.size.width,
                                this.size.height,
                                0.dp.toPx(),
                                0.dp.toPx(),
                                paint
                            )

                        }

                    }
                    .background(Color.Black)
                    .width(340.dp)
                    .height(53.dp)


            ){
                Text(
                    text = randomized[3],
                    color = Color.White,

                    )
            }


        }


    }

}

@Composable
fun Leaderboard() {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        val shadowColorPurple = colorRed.copy(alpha = 0.5f).toArgb()
        val transparentPurple = colorRed.copy(alpha = 0f).toArgb()


        Card(
            backgroundColor = "#000000".color,
            modifier = Modifier
                .padding(top = 0.dp)
                .align(Alignment.Center)
                .drawBehind {
                    drawIntoCanvas {
                        val paint = Paint()
                        val frameworkPaint = paint.asFrameworkPaint()
                        frameworkPaint.color = transparentPurple
                        it.drawRoundRect(
                            0f,
                            0f,
                            this.size.width,
                            this.size.height,
                            0.dp.toPx(),
                            0.dp.toPx(),
                            paint
                        )
                    }
                }
                .clip(RoundedCornerShape(18.dp))
                .background("#CC3CFF".color)
                .size(310.dp, 635.dp)


        ) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = "Leaderboard",
                    color = colorRed,
                    fontFamily = gameFont,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 35.dp, bottom = 5.dp)

                )

                if (Manager.desc.isNotEmpty()) {
                    Column(Modifier.fillMaxSize()) {
                        for (i in Manager.desc) {


                            Row(
                                Modifier

                                    .padding(top = 25.dp)
                            ) {
                                Text(
                                    text = (Manager.desc.indexOf(i) + 1).toString() + " - " + i.name,
                                    color = "#FFFFFF".color,
                                    fontFamily = gameFont,
                                    fontSize = 10.sp,
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier
                                        .padding(top = 0.dp, start = 35.dp)

                                )

                                Text(
                                    text = i.points.toString(),
                                    color = colorYellow,
                                    fontFamily = gameFont,
                                    textAlign = TextAlign.End,
                                    fontSize = 10.sp,
                                    modifier = Modifier
                                        .padding(top = 0.dp, start = 100.dp)

                                )
                            }


                        }

                    }
                }


            }
        }
    }

}
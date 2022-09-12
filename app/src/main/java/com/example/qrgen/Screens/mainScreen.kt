package com.example.qrgen.Screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import androidx.compose.material.Card

@OptIn(ExperimentalComposeUiApi::class)
@Composable

fun mainScreen() {

    val testState = remember {
        mutableStateOf("")
    }

    var bitmap : Bitmap? = null

    val state = remember {
        mutableStateOf(false)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xF748086F)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(15.dp),
                shape = RoundedCornerShape(CornerSize(20.dp)), backgroundColor = Color.White, elevation = 10.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    if (state.value) {

                        Image(bitmap = bitmap!!.asImageBitmap(), contentDescription = "qr code")
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                    OutlinedTextField(
                        value = testState.value,
                        onValueChange = {
                            testState.value = it
                            state.value = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        enabled = true,
                        label = {
                            Text(text = "Enter Url or data", color = Color.Black)
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Blue,
                            cursorColor = Color.Black,
                            disabledLabelColor = Color.Black,
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(45.dp))

                    Button(
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            val content = testState.value
                            val writer = QRCodeWriter()
                            val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512)
                            val width = bitMatrix.width
                            val height = bitMatrix.height
                            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                            for (x in 0 until width) {
                                for (y in 0 until height) {
                                    bitmap!!.setPixel(
                                        x, y, if (bitMatrix.get(x, y))
                                            android.graphics.Color.BLACK else android.graphics.Color.WHITE
                                    )
                                }
                            }
                            state.value = true
                        }, colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xF748086F)
                        ),
                        modifier = Modifier
                            .padding(10.dp)
                            .width(160.dp)
                            .height(45.dp),

                        ) {
                        Text(
                            text = "Generate",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                }
            }

        }
    }


}

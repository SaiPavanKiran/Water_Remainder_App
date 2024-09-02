package com.rspk.water_remainder_app.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.rspk.water_remainder_app.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("SimpleDateFormat")
@Composable
fun ActivityListGrids(
    modifier: Modifier = Modifier,
    list: List<String>,
){
    Column(
        modifier = modifier,
    ) {
        TextInput(
            text = R.string.current_date,
            textInput = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = dimensionResource(id = R.dimen.grid_size)),
            contentPadding = PaddingValues(top = dimensionResource(id = R.dimen.padding30), bottom = dimensionResource(id = R.dimen.padding50)),
        ) {

            itemsIndexed(list) { index, it ->
                CardForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(id = R.dimen.padding15))
                ) {
                    ActivityScreenTextDisplay(
                        index = index,
                        time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(it.substringBefore(":").toLong()).toString(),
                        waterAmount = it.substringAfter(":").substringBeforeLast(":"),
                        endString = it.substringAfterLast(":")
                    )
                }
            }
        }
    }
}
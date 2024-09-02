package com.rspk.water_remainder_app.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.rspk.water_remainder_app.R

@Composable
fun CardForm(
    modifier: Modifier = Modifier,
    cardContent: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier = modifier,
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = dimensionResource(id = R.dimen.elevation10)),
            shape = RoundedCornerShape(size = dimensionResource(id = R.dimen.size15)),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
        ) {
            cardContent()
        }
    }
}
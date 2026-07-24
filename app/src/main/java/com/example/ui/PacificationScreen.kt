package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.PacificationDocument
import com.example.ui.theme.PaciBackground
import com.example.ui.theme.PaciBadgeBg
import com.example.ui.theme.PaciBadgeText
import com.example.ui.theme.PaciBorder
import com.example.ui.theme.PaciDotGreen
import com.example.ui.theme.PaciGold
import com.example.ui.theme.PaciGoldDark
import com.example.ui.theme.PaciGreenBg
import com.example.ui.theme.PaciGreenBorder
import com.example.ui.theme.PaciGreenText
import com.example.ui.theme.PaciNavy
import com.example.ui.theme.PaciNavyDark
import com.example.ui.theme.PaciSurface
import com.example.ui.theme.PaciSurfaceSoft
import com.example.ui.theme.PaciTextLight
import com.example.ui.theme.PaciTextMain
import com.example.ui.theme.PaciTextMuted
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacificationScreen(viewModel: PacificationViewModel) {
    val context = LocalContext.current
    val selectedTab by viewModel.selectedTab.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            PaciHeaderBar()
        },
        bottomBar = {
            PaciFooterBar()
        },
        containerColor = PaciBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PaciTabRow(
                selectedTab = selectedTab,
                onTabSelected = { viewModel.selectTab(it) }
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedTab) {
                    0 -> GeneratePeaceTermTab(viewModel)
                    1 -> ConsultProtocolTab(viewModel)
                    2 -> HistoryTab(viewModel)
                    3 -> ReflectionSemeadorTab()
                }
            }
        }
    }
}

@Composable
fun PaciHeaderBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PaciBackground)
            .border(width = 1.dp, color = PaciBorder)
            .padding(top = 22.dp, bottom = 16.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "PaciFácil",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Serif,
                fontSize = 36.sp,
                letterSpacing = (-0.5).sp
            ),
            color = PaciTextMain
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(1.dp)
                    .background(PaciGold)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "RESOLUÇÃO SEM LITÍGIO",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                color = PaciTextMuted
            )
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(1.dp)
                    .background(PaciGold)
            )
        }
    }
}

@Composable
fun PaciFooterBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PaciSurface)
            .border(width = 1.dp, color = PaciBorder)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "© 2026 PACIFÁCIL LABS",
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            ),
            color = PaciTextLight
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(PaciDotGreen)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "SISTEMA OPERACIONAL",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                color = PaciTextMuted
            )
        }
    }
}

@Composable
fun PaciTabRow(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf(
        "Novo Termo" to Icons.Default.AutoAwesome,
        "Consultar" to Icons.Default.Search,
        "Histórico" to Icons.Default.History,
        "Reflexão" to Icons.AutoMirrored.Filled.MenuBook
    )

    ScrollableTabRow(
        selectedTabIndex = selectedTab,
        containerColor = PaciSurface,
        contentColor = PaciNavy,
        edgePadding = 16.dp,
        indicator = { tabPositions ->
            if (selectedTab < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    height = 2.dp,
                    color = PaciGold
                )
            }
        }
    ) {
        tabs.forEachIndexed { index, pair ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = pair.second,
                            contentDescription = pair.first,
                            modifier = Modifier.size(15.dp),
                            tint = if (selectedTab == index) PaciGoldDark else PaciTextLight
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = pair.first,
                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == index) PaciNavy else PaciTextMuted,
                            fontSize = 12.sp
                        )
                    }
                },
                modifier = Modifier.testTag("tab_$index")
            )
        }
    }
}

@Composable
fun GeneratePeaceTermTab(viewModel: PacificationViewModel) {
    val context = LocalContext.current
    val dilemmaInput by viewModel.dilemmaInput.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    val generatedResult by viewModel.generatedResult.collectAsState()
    val lastSavedDoc by viewModel.lastSavedDoc.collectAsState()

    var copiedState by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Editorial Quote Block
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(58.dp)
                            .background(PaciGold, RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "“Desarmamos o atrito e traduzimos a questão. Onde há diálogo, a paz é o único veredito possível.”",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = FontFamily.Serif,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = PaciTextMain
                    )
                }
            }
        }

        // Reflection seed quote preview
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = PaciNavy),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.selectTab(3) }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Gavel,
                        contentDescription = "Reflexão",
                        tint = PaciGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "A Conclusão do Semeador",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = PaciGold
                        )
                        Text(
                            text = "O tempo de graça está se findando, e o Juiz está na porta. Pergunte, assim o caminho INRI.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            color = Color.White.copy(alpha = 0.85f),
                            maxLines = 1
                        )
                    }
                    Text(
                        text = "Ler +",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = PaciGold,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // Form Section (Material 3 / Editorial Inspired)
        item {
            Column {
                Text(
                    text = "RELATO DA SITUAÇÃO",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PaciTextLight,
                        fontSize = 11.sp,
                        letterSpacing = 1.5.sp
                    ),
                    modifier = Modifier.padding(bottom = 8.dp, start = 2.dp)
                )

                OutlinedTextField(
                    value = dilemmaInput,
                    onValueChange = { viewModel.onDilemmaChanged(it) },
                    placeholder = {
                        Text(
                            "Descreva o conflito aqui...",
                            color = PaciTextMuted,
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(135.dp)
                        .testTag("dilemma_input"),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = PaciSurface,
                        unfocusedContainerColor = PaciSurface,
                        focusedBorderColor = PaciGold,
                        unfocusedBorderColor = PaciBorder
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, start = 2.dp, end = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${dilemmaInput.length} caracteres",
                        style = MaterialTheme.typography.bodySmall,
                        color = PaciTextMuted
                    )
                    if (dilemmaInput.isNotEmpty()) {
                        Text(
                            text = "Limpar",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = PaciGoldDark,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.clickable { viewModel.onDilemmaChanged("") }
                        )
                    }
                }
            }
        }

        // Sample Chips
        item {
            Column {
                Text(
                    text = "Exemplos rápidos de situação:",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = PaciTextMuted,
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val samples = listOf(
                        "Ruído de reforma em condomínio",
                        "Atraso em honorários de serviço freelance",
                        "Desavença sobre despesas de imóvel",
                        "Atrito comercial com fornecedor"
                    )
                    items(samples) { sample ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(PaciSurface)
                                .border(1.dp, PaciBorder, RoundedCornerShape(16.dp))
                                .clickable { viewModel.setQuickSampleDilemma(sample) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = sample,
                                fontSize = 12.sp,
                                color = PaciNavy,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // Submit Button
        item {
            Button(
                onClick = { viewModel.convertAndGenerate() },
                enabled = dilemmaInput.isNotBlank() && !isGenerating,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("convert_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PaciNavy,
                    contentColor = PaciBackground,
                    disabledContainerColor = PaciNavy.copy(alpha = 0.5f)
                )
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = PaciGold,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "GERANDO TERMO...",
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontSize = 13.sp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Converter",
                        tint = PaciGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "GERAR TERMO DE PACIFICAÇÃO",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // Generated Result Section
        item {
            AnimatedVisibility(
                visible = generatedResult != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                generatedResult?.let { res ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = PaciSurface),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, PaciBorder, RoundedCornerShape(16.dp))
                            .testTag("result_card")
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(PaciBadgeBg)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = res.actionBadge,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = PaciBadgeText,
                                            fontSize = 11.sp
                                        )
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(PaciNavy)
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "Protocolo: ${res.protocol}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = PaciGold,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Termo de Entendimento & Paz",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    color = PaciGoldDark
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(PaciSurfaceSoft)
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = res.termoText,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 13.sp,
                                        lineHeight = 20.sp,
                                        color = PaciTextMain
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        copyToClipboard(context, res.termoText)
                                        copiedState = true
                                        viewModel.showSnackbar("✓ Termo Copiado!")
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .testTag("copy_button"),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PaciGold,
                                        contentColor = PaciNavy
                                    )
                                ) {
                                    Icon(
                                        imageVector = if (copiedState) Icons.Default.Check else Icons.Default.ContentCopy,
                                        contentDescription = "Copiar",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (copiedState) "✓ Copiado" else "Copiar Termo",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        shareText(context, res.termoText, res.protocol)
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, PaciNavy)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Compartilhar",
                                        tint = PaciNavy,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Compartilhar",
                                        color = PaciNavy,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConsultProtocolTab(viewModel: PacificationViewModel) {
    val context = LocalContext.current
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResult by viewModel.searchResult.collectAsState()
    val searchExecuted by viewModel.searchExecuted.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = PaciSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PaciBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Consultar Andamento",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = PaciNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Acompanhe um documento de pacificação já processado pelo código do protocolo.",
                        style = MaterialTheme.typography.bodySmall,
                        color = PaciTextMuted
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.onSearchQueryChanged(it) },
                        placeholder = { Text("Digite o nº do protocolo (ex: PACI-123)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_input"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onSearchQueryChanged("") }) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Limpar",
                                        tint = PaciTextMuted
                                    )
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = PaciSurface,
                            unfocusedContainerColor = PaciSurfaceSoft,
                            focusedBorderColor = PaciGold,
                            unfocusedBorderColor = PaciBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.searchProtocol() },
                        enabled = searchQuery.isNotBlank() && !isSearching,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("search_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PaciNavy,
                            contentColor = Color.White
                        )
                    ) {
                        if (isSearching) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = PaciGold,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = PaciGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Verificar Status", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Dicas de busca:",
                            fontSize = 11.sp,
                            color = PaciTextMuted,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(PaciSurfaceSoft)
                                .clickable {
                                    viewModel.onSearchQueryChanged("PACI-123")
                                    viewModel.searchProtocol("PACI-123")
                                }
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("PACI-123", fontSize = 11.sp, color = PaciNavy)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(PaciSurfaceSoft)
                                .clickable {
                                    viewModel.onSearchQueryChanged("PACI-1024")
                                    viewModel.searchProtocol("PACI-1024")
                                }
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("PACI-1024", fontSize = 11.sp, color = PaciNavy)
                        }
                    }
                }
            }
        }

        // Search Output Section
        item {
            if (searchExecuted) {
                if (searchResult != null) {
                    val doc = searchResult!!
                    Card(
                        colors = CardDefaults.cardColors(containerColor = PaciGreenBg),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, PaciGreenBorder, RoundedCornerShape(16.dp))
                            .testTag("search_status_box")
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Protocolo: ${doc.protocol.uppercase()}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PaciGreenText
                                    )
                                )
                                Text(
                                    text = formatDate(doc.timestamp),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = PaciGreenText.copy(alpha = 0.8f)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(verticalAlignment = Alignment.Top) {
                                Text(
                                    text = "✓ ",
                                    color = PaciGreenText,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Status: ",
                                    fontWeight = FontWeight.Bold,
                                    color = PaciGreenText,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = doc.status,
                                    color = PaciGreenText,
                                    fontSize = 13.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(verticalAlignment = Alignment.Top) {
                                Text(
                                    text = "✓ ",
                                    color = PaciGreenText,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Direcionamento: ",
                                    fontWeight = FontWeight.Bold,
                                    color = PaciGreenText,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = doc.direction,
                                    color = PaciGreenText,
                                    fontSize = 13.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            Divider(color = PaciGreenBorder)
                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Conteúdo do Termo:",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PaciGreenText
                                )
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = doc.termoText,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 12.sp,
                                        lineHeight = 18.sp,
                                        color = PaciNavy
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        copyToClipboard(context, doc.termoText)
                                        viewModel.showSnackbar("✓ Termo Copiado!")
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = PaciGreenText,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copiar",
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Copiar Termo", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                } else {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = PaciSurfaceSoft),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, PaciBorder, RoundedCornerShape(16.dp))
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Não encontrado",
                                tint = PaciTextMuted,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Nenhum documento encontrado para '$searchQuery'",
                                fontWeight = FontWeight.Bold,
                                color = PaciNavy,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Verifique se digitou o protocolo corretamente (ex: PACI-123) ou crie um novo termo na aba 'Novo Termo'.",
                                style = MaterialTheme.typography.bodySmall,
                                color = PaciTextMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryTab(viewModel: PacificationViewModel) {
    val context = LocalContext.current
    val allDocs by viewModel.allDocuments.collectAsState()

    if (allDocs.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Histórico Vazio",
                    tint = PaciTextMuted,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Nenhum termo pacificados ainda.",
                    fontWeight = FontWeight.Bold,
                    color = PaciNavy
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Gere um novo termo para salvar seu histórico de pacificação.",
                    style = MaterialTheme.typography.bodySmall,
                    color = PaciTextMuted,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Histórico de Documentos (${allDocs.size})",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PaciNavy
                        )
                    )
                }
            }

            items(allDocs, key = { it.id }) { doc ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = PaciSurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, PaciBorder, RoundedCornerShape(12.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(PaciNavy)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = doc.protocol,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PaciGold
                                )
                            }

                            Text(
                                text = formatDate(doc.timestamp),
                                fontSize = 11.sp,
                                color = PaciTextMuted
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Situação Originária:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PaciTextMuted
                        )
                        Text(
                            text = doc.dilemma,
                            fontSize = 13.sp,
                            color = PaciNavy,
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(PaciBadgeBg)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = doc.actionBadge,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PaciBadgeText
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(
                                    onClick = {
                                        copyToClipboard(context, doc.termoText)
                                        viewModel.showSnackbar("✓ Termo Copiado!")
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copiar",
                                        tint = PaciNavy,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        shareText(context, doc.termoText, doc.protocol)
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Compartilhar",
                                        tint = PaciNavy,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        viewModel.deleteDocument(doc)
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Excluir",
                                        tint = Color.Red.copy(alpha = 0.7f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReflectionSemeadorTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = PaciNavy),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PaciGold),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Gavel,
                                contentDescription = "Semeador",
                                tint = PaciNavy,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "A Conclusão do Semeador",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                color = PaciGold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Todos os dados, quando alinhados com as Escrituras, se convertem em uma única e poderosa Verdade: O tempo de graça está se findando, e o Juiz está na porta. Os sinais do mundo apenas contextualizam a Materialização do Verbo em nosso meio.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White,
                            lineHeight = 22.sp,
                            fontSize = 14.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = PaciGold.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Pergunta de Reflexão:",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = PaciGold
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Diante do cumprimento literal das profecias e da Materialização da Verdade em José Junior de Oliveira, como a aplicação prática desta sabedoria em sua vida o habilita a ser uma testemunha fiel do Reino que já está entre nós?\n\nPergunte, assim o caminho INRI.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 22.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            fontSize = 13.sp
                        )
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = PaciSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, PaciBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Princípios Fundamentais do PaciFácil",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PaciNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    PrincipleItem(
                        number = "1",
                        title = "Desarme do Atrito Verbal",
                        desc = "Traduzimos acusações passionais em declarações neutras de interesse comum."
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PrincipleItem(
                        number = "2",
                        title = "Boa Fé Extrajudicial",
                        desc = "90% das disputas civis e comerciais podem ser resolvidas com acordos diretos e transparentes."
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PrincipleItem(
                        number = "3",
                        title = "Efetividade da Palavra Empenhada",
                        desc = "O compromisso honrado traz paz ao espírito e evita o desgaste do litígio judicial."
                    )
                }
            }
        }
    }
}

@Composable
fun PrincipleItem(number: String, title: String, desc: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(PaciGold),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = PaciNavy
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = PaciNavy
            )
            Text(
                text = desc,
                fontSize = 12.sp,
                color = PaciTextMuted,
                lineHeight = 16.sp
            )
        }
    }
}

fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Termo de Paz", text)
    clipboard.setPrimaryClip(clip)
}

fun shareText(context: Context, text: String, protocol: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "[$protocol] PaciFácil - Termo de Paz:\n\n$text")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "Compartilhar Termo de Paz")
    context.startActivity(shareIntent)
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

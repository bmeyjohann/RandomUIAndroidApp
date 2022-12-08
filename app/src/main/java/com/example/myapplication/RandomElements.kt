@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.*
import kotlin.random.Random

@Composable
fun RandomIE(state: State) {
    val ieType by remember { mutableStateOf(probsToIndex(*probsIE)) }

    when (ieType) {
        0 -> RandomButton(state = state)
        1 -> RandomCheckbox(state = state)
        2 -> RandomRadioButton(state = state)
        3 -> RandomSwitch(state = state)
        4 -> RandomTextField(state = state)
        5 -> RandomSlider(state = state)
    }
}

@Composable
fun RandomBottomBar(state: State) {
    val numOfEntries by remember { mutableStateOf(Random.nextInt(3, 6)) }
    val selectedEntry by remember { mutableStateOf(Random.nextInt(1, numOfEntries + 1)) }
    val visible by remember { mutableStateOf(probToBool(probBottomBar))}

    if(visible) {
        NavigationBar {
            for (i in 1..numOfEntries) {
                RandomNavigationBarItem(i == selectedEntry, state)
            }
        }
    }
}

@Composable
fun RowScope.RandomNavigationBarItem(selected: Boolean, state: State) {
    val id by remember { mutableStateOf(state.registry.registerElement("NavigationBarItem")) }
    val text by remember { mutableStateOf(randomText(1, 7)) }

    if(state.mask) {
        if(state.idOfMaskElement == id) {
            NavigationBarItem(
                selected = false,
                icon = {
                    RandomIcon(
                        state,
                        id,
                        iconSet.random()
                    )
                       },
                label = {
                    Text(
                        text = text,
                        color = Color.White
                    )
                        },
                onClick = {},
                colors = NavigationBarItemDefaults.colors(Color.White),
                modifier = Modifier.background(Color.White)
            )
        } else {
            NavigationBarItem(
                selected = false,
                icon = {
                    RandomIcon(
                        state,
                        id,
                        iconSet.random()
                    )
                },
                label = {
                    Text(
                        text = text,
                        color = Color.Black
                    )
                },
                onClick = {},
                colors = NavigationBarItemDefaults.colors(Color.Black)
            )
        }
    } else {
        NavigationBarItem(
            selected = selected,
            icon = {
                RandomIcon(
                    state,
                    id,
                    iconSet.random()
                )
            },
            label = {
                Text(
                    text = text
                )
            },
            onClick = {},
            modifier = Modifier.onCondition(state.mask && id == state.idOfMaskElement, Modifier.background(Color.White))
        )
    }
}

@Composable
fun RandomFloatingActionButton(state: State) {
    val visible by remember { mutableStateOf(probToBool(probFloatingActionButton)) }

    if(visible) {
        val id by remember { mutableStateOf(state.registry.registerElement("FloatingActionButton")) }
        if(state.mask) {
            if(state.idOfMaskElement == id) {
                FloatingActionButton(
                    onClick = {},
                    containerColor = Color.White,
                    contentColor = Color.White,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                ) {}
            } else {
                FloatingActionButton(
                    onClick = {},
                    containerColor = Color.Black,
                    contentColor = Color.Black,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp)
                ) {}
            }
        } else {
            FloatingActionButton(
                onClick = {}
            ) {
                RandomIcon(
                    state,
                    id,
                    iconSet.random()
                )
            }
        }
    }
}

@Composable
fun RandomTopAppBar(state: State) {
    SmallTopAppBar(
        title = { Text(text = randomText(2 ,7)) },
        navigationIcon = {
            val navIcons = setOf(Icons.Filled.Menu, Icons.Filled.ArrowBack)
            val icon = navIcons.random()
            val className = if(icon.name == "Filled.Menu") "Menu" else "BackButton"

            val id by remember{ mutableStateOf(state.registry.registerElement(className)) }

            IconButton(
                onClick = {},
                modifier = Modifier.then(if(state.mask && id == state.idOfMaskElement) Modifier.background(Color.White) else Modifier)
            ) {
                RandomIcon(
                    state,
                    id,
                    icon
                )
            }
        },
        actions = {
            val id by remember{ mutableStateOf(state.registry.registerElement("MoreOptions")) }

            IconButton(
                onClick = {},
                modifier = Modifier.then(if(state.mask && id == state.idOfMaskElement) Modifier.background(Color.White) else Modifier)
            ) {
                RandomIcon(
                    state,
                    id,
                    Icons.Filled.MoreVert
                )
            }
        }
    )
}

@Composable
fun RandomButton(state: State) {
    val id by remember { mutableStateOf(state.registry.registerElement("Button")) }
    val buttonIcon by remember { mutableStateOf(probToBool(probButtonIcon)) }
    val buttonType by remember { mutableStateOf(probsToIndex(*probsButtonType)) }
    val text by remember { mutableStateOf(randomText(3 ,7)) }
    val fillAvailableSpace by remember { mutableStateOf(probToBool(probButtonFillAvailableSpace)) }
    val cornerRadius by remember { mutableStateOf(clipIntInclusive(Random.nextInt(100)-25, 0, 50))}

    if(state.mask) {
        if(state.idOfMaskElement == id) {
            if(buttonType != 2) {
                Button(
                    enabled = true,
                    onClick = {},
                    content = {
                        if (buttonIcon) {
                            RandomIcon(
                                state,
                                id,
                                iconSet.random(),
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        }
                        Text(text, color = Color.White)
                    },
                    shape = RoundedCornerShape(cornerRadius),
                    colors = ButtonDefaults.buttonColors(Color.White),
                    modifier = Modifier.onCondition(fillAvailableSpace, Modifier.fillMaxWidth(1.0f))
                )
            } else {
                TextButton(
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(Color.White),
                    onClick = {},
                    content = {
                        Text(text, color = Color.White)
                    },
                    shape = RectangleShape,
                    modifier = Modifier.background(Color.White).border(13.dp, Color.Black)
                )
            }
        } else {
            if(buttonType != 2) {
                Button(
                    enabled = true,
                    onClick = {},
                    content = {
                        if (buttonIcon) {
                            RandomIcon(
                                state,
                                id,
                                iconSet.random(),
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        }
                        Text(text, color = Color.Black)
                    },
                    shape = RoundedCornerShape(cornerRadius),
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    modifier = Modifier.onCondition(fillAvailableSpace, Modifier.fillMaxWidth(1.0f))
                )
            } else {
                TextButton(
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(Color.Black),
                    onClick = {},
                    content = {
                        Text(text, color = Color.Black)
                    },
                    shape = RectangleShape
                )
            }
        }
    } else {
        when(buttonType) {
            0 -> {
                Button(
                    enabled = true,
                    onClick = {},
                    content = {
                        if (buttonIcon) {
                            RandomIcon(
                                state,
                                id,
                                iconSet.random(),
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        }
                        Text(text)
                    },
                    shape = RoundedCornerShape(cornerRadius),
                    modifier = Modifier.onCondition(fillAvailableSpace, Modifier.fillMaxWidth(1.0f))
                )
            }
            1 -> {
                OutlinedButton(
                    enabled = true,
                    onClick = {},
                    content = {
                        if (buttonIcon) {

                            RandomIcon(
                                state,
                                id,
                                iconSet.random(),
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        }
                        Text(text)
                    },
                    shape = RoundedCornerShape(cornerRadius),
                    modifier = Modifier.onCondition(fillAvailableSpace, Modifier.fillMaxWidth(1.0f))
                )
            }
            else -> {
                TextButton(
                    enabled = true,
                    onClick = {},
                    content = {
                        Text(text)
                    },
                    shape = RectangleShape
                )
            }
        }
    }
}

@Composable
fun RandomCheckbox(state: State) {
    val id by remember { mutableStateOf(state.registry.registerElement("Checkbox")) }
    val checked by remember { mutableStateOf(probToBool(probCheckboxChecked)) }
    val text by remember { mutableStateOf(randomText(4, 7)) }

    if(state.mask) {
        if(state.idOfMaskElement == id) {
            Spacer(modifier = Modifier.padding(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.padding(5.dp))
                Checkbox(
                    checked = true,
                    onCheckedChange = null,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.White,
                        uncheckedColor = Color.White,
                        checkmarkColor = Color.White,
                        disabledIndeterminateColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = text)
            }
            Spacer(modifier = Modifier.padding(2.dp))
        } else {
            Spacer(modifier = Modifier.padding(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.padding(5.dp))
                Checkbox(
                    checked = checked,
                    onCheckedChange = null,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Black,
                        uncheckedColor = Color.Black,
                        checkmarkColor = Color.Black,
                        disabledIndeterminateColor = Color.Black
                    )
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = text)
            }
            Spacer(modifier = Modifier.padding(2.dp))
        }
    } else {
        Spacer(modifier = Modifier.padding(2.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.padding(5.dp))
            Checkbox(
                checked = checked,
                onCheckedChange = null
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = text)
        }
        Spacer(modifier = Modifier.padding(2.dp))
    }
}

@Composable
fun RandomRadioButton(state: State) {
    val id by remember { mutableStateOf(state.registry.registerElement("RadioButton")) }
    val selected by remember { mutableStateOf(probToBool(probRadioButtonSelected)) }
    val text by remember { mutableStateOf(randomText(4, 7)) }

    if(state.mask) {
        if(state.idOfMaskElement == id) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = false,
                    onClick = {},
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.White,
                        unselectedColor = Color.White
                    ),
                    modifier = Modifier
                        .background(Color.White, CircleShape)
                        .border(14.dp, Color.Black, CircleShape)
                )
                Text(text = text)
            }
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = true,
                    onClick = {},
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color.Black,
                        unselectedColor = Color.Black
                    ),
                    modifier = Modifier
                        .background(Color.Transparent, CircleShape)
                        .border(14.dp, Color.Transparent, CircleShape)
                )
                Text(text = text)
            }
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = {},
                modifier = Modifier
                    .background(Color.Transparent, CircleShape)
                    .border(14.dp, Color.Transparent, CircleShape)
            )
            Text(text = text)
        }
    }
}

@Composable
fun RandomSlider(state: State) {
    val id by remember { mutableStateOf(state.registry.registerElement("Slider")) }
    val pos by remember { mutableStateOf(Random.nextFloat()) }
    val width by remember { mutableStateOf(Random.nextFloat() * 0.7f + 0.3f) }

    if(state.mask) {
        if (state.idOfMaskElement == id) {
            Slider(
                value = pos,
                onValueChange = {},
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTickColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth(width)
            )
        } else {
            Slider(
                value = pos,
                onValueChange = {},
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = Color.Transparent,
                    activeTickColor = Color.Transparent,
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth(width)
            )
        }
    } else {
        Slider(
            value = pos,
            onValueChange = {},
            valueRange = 0f..1f,
            modifier = Modifier.fillMaxWidth(width)
        )
    }

}

@Composable
fun RandomSwitch(state: State) {
    val id by remember { mutableStateOf(state.registry.registerElement("Switch")) }
    val text by remember { mutableStateOf(randomText(4, 7)) }
    val checked by remember { mutableStateOf(probToBool(probSwitchChecked)) }

    if(state.mask) {
        if(state.idOfMaskElement == id) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = text)
                Switch(
                    checked = checked,
                    onCheckedChange = {},
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color.White,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.White
                    )
                )
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = text)
                Switch(
                    checked = checked,
                    onCheckedChange = {},
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.Black,
                        checkedTrackColor = Color.Black,
                        uncheckedThumbColor = Color.Black,
                        uncheckedTrackColor = Color.Black
                    )
                )
            }
        }
    } else {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = text)
            Switch(
                checked = checked,
                onCheckedChange = {}
            )
        }
    }
}

@Composable
fun RandomTextField(state: State) {
    val id by remember { mutableStateOf(state.registry.registerElement("TextField")) }
    val text by remember { mutableStateOf(randomText(2, 7, true)) }
    val label by remember { mutableStateOf(randomText(2, 7, true)) }
    val outlined by remember { mutableStateOf(Random.nextBoolean()) }

    if(state.mask) {
        if(state.idOfMaskElement == id) {
            if(outlined) {
                OutlinedTextField(
                    value = text,
                    onValueChange = {},
                    label = { Text(label) },
                    colors = textFieldColors(
                        textColor = Color.White,
                        containerColor = Color.White,
                        disabledTextColor = Color.White,
                        cursorColor = Color.White,
                        errorCursorColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        disabledIndicatorColor = Color.White,
                        errorIndicatorColor = Color.White,
                        disabledLeadingIconColor = Color.White,
                        errorLeadingIconColor = Color.White,
                        disabledTrailingIconColor = Color.White,
                        errorTrailingIconColor = Color.White,
                        focusedLabelColor = Color.Transparent,
                        unfocusedLabelColor = Color.Transparent,
                        disabledLabelColor = Color.Transparent,
                        errorLabelColor = Color.White,
                        placeholderColor = Color.White,
                        disabledPlaceholderColor = Color.White
                    )
                )
            } else {
                TextField(
                    value = text,
                    onValueChange = {},
                    label = { Text(label) },
                    colors = textFieldColors(
                        textColor = Color.White,
                        containerColor = Color.White,
                        disabledTextColor = Color.White,
                        cursorColor = Color.White,
                        errorCursorColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White,
                        disabledIndicatorColor = Color.White,
                        errorIndicatorColor = Color.White,
                        disabledLeadingIconColor = Color.White,
                        errorLeadingIconColor = Color.White,
                        disabledTrailingIconColor = Color.White,
                        errorTrailingIconColor = Color.White,
                        focusedLabelColor = Color.Transparent,
                        unfocusedLabelColor = Color.Transparent,
                        disabledLabelColor = Color.Transparent,
                        errorLabelColor = Color.White,
                        placeholderColor = Color.White,
                        disabledPlaceholderColor = Color.White
                    )

                )
            }
        } else {
            if(outlined) {
                OutlinedTextField(
                    value = text,
                    onValueChange = {},
                    label = { Text(label) },
                    colors = textFieldColors(
                        textColor = Color.Black,
                        containerColor = Color.Black,
                        disabledTextColor = Color.Black,
                        cursorColor = Color.Black,
                        errorCursorColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.Black,
                        disabledIndicatorColor = Color.Black,
                        errorIndicatorColor = Color.Black,
                        disabledLeadingIconColor = Color.Black,
                        errorLeadingIconColor = Color.Black,
                        disabledTrailingIconColor = Color.Black,
                        errorTrailingIconColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        disabledLabelColor = Color.Black,
                        errorLabelColor = Color.Black,
                        placeholderColor = Color.Black,
                        disabledPlaceholderColor = Color.Black
                    )

                )
            } else {
                TextField(
                    value = text,
                    onValueChange = {},
                    label = { Text(label) },
                    colors = textFieldColors(
                        textColor = Color.Black,
                        containerColor = Color.Black,
                        disabledTextColor = Color.Black,
                        cursorColor = Color.Black,
                        errorCursorColor = Color.Black,
                        focusedIndicatorColor = Color.Black,
                        unfocusedIndicatorColor = Color.Black,
                        disabledIndicatorColor = Color.Black,
                        errorIndicatorColor = Color.Black,
                        disabledLeadingIconColor = Color.Black,
                        errorLeadingIconColor = Color.Black,
                        disabledTrailingIconColor = Color.Black,
                        errorTrailingIconColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                        disabledLabelColor = Color.Black,
                        errorLabelColor = Color.Black,
                        placeholderColor = Color.Black,
                        disabledPlaceholderColor = Color.Black
                    )

                )
            }
        }
    } else {
        if(outlined) {
            OutlinedTextField(
                value = text,
                onValueChange = {},
                label = { Text(label) }

            )
        } else {
            TextField(
                value = text,
                onValueChange = {},
                label = { Text(label) }

            )
        }
    }
}

@Composable
fun RandomIcon(state: State, id: Int, iconToDisplay: ImageVector, modifier: Modifier = Modifier) {
    if (state.mask) {
        if (id == state.idOfMaskElement) {
            Icon(
                imageVector = iconToDisplay,
                contentDescription = null,
                tint = Color.White,
                modifier = modifier
            )
        } else {
            Icon(
                imageVector = iconToDisplay,
                contentDescription = null,
                tint = Color.Black,
                modifier = modifier
            )
        }
    } else {
        Icon(
            imageVector = iconToDisplay,
            contentDescription = null,
            modifier = modifier
        )
    }
}
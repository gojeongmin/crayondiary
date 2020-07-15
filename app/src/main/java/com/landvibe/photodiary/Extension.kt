package com.landvibe.photodiary

import java.text.SimpleDateFormat
import java.util.*

//Date함수에 추가적인 기능을 구현해서 프로젝트에서 활용 할 수 있도록 해준다.
fun Date.formatYYYYMMDD(): String {
    return SimpleDateFormat("yyyy.MM.dd, HH:mm:ss", Locale.getDefault()).format(this)
}
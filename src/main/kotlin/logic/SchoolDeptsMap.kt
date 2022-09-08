package logic

import general.School
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

val b =
    "{\"01\":{\"code\":\"01\",\"name\":\"School of Arts and Sciences, New Brunswick\",\"depts\":[\"013\",\"014\",\"050\",\"070\",\"074\",\"078\",\"082\",\"090\",\"098\",\"119\",\"146\",\"160\",\"165\",\"175\",\"185\",\"190\",\"195\",\"198\",\"202\",\"220\",\"351\",\"354\",\"355\",\"356\",\"358\",\"359\",\"360\",\"377\",\"381\",\"420\",\"447\",\"450\",\"460\",\"470\",\"489\",\"490\",\"505\",\"506\",\"508\",\"510\",\"512\",\"556\",\"558\",\"560\",\"563\",\"565\",\"574\",\"580\",\"590\",\"595\",\"615\",\"640\",\"667\",\"685\",\"694\",\"713\",\"723\",\"730\",\"750\",\"787\",\"790\",\"810\",\"830\",\"840\",\"860\",\"904\",\"920\",\"940\",\"955\",\"960\",\"974\",\"988\",\"991\"]},\"03\":{\"code\":\"03\",\"name\":\"Military Education (ROTC)\",\"depts\":[\"690\",\"691\",\"692\"]},\"04\":{\"code\":\"04\",\"name\":\"School of Communication and Information, Undergraduate\",\"depts\":[\"189\",\"192\",\"547\",\"567\"]},\"05\":{\"code\":\"05\",\"name\":\"School of Education (GSE), Undergraduate program\",\"depts\":[\"300\"]},\"07\":{\"code\":\"07\",\"name\":\"Mason Gross School of the Arts, Undergraduate\",\"depts\":[\"080\",\"081\",\"203\",\"206\",\"207\",\"211\",\"557\",\"700\",\"701\",\"965\",\"966\"]},\"08\":{\"code\":\"08\",\"name\":\"Mason Gross School of the Arts, Graduate\",\"depts\":[\"081\",\"208\",\"702\",\"703\",\"966\"]},\"09\":{\"code\":\"09\",\"name\":\"School of Social Work, Undergraduate\",\"depts\":[\"910\"]},\"10\":{\"code\":\"10\",\"name\":\"Bloustein School of Planning and Public Policy, Undergraduate\",\"depts\":[\"501\",\"652\",\"762\",\"775\",\"832\",\"833\",\"843\",\"971\",\"975\"]},\"11\":{\"code\":\"11\",\"name\":\"School of Environmental and Biological Sciences (Cook College)\",\"depts\":[\"015\",\"020\",\"035\",\"067\",\"090\",\"115\",\"117\",\"126\",\"193\",\"216\",\"370\",\"373\",\"374\",\"375\",\"400\",\"550\",\"554\",\"573\",\"607\",\"628\",\"670\",\"680\",\"709\",\"776\"]},\"13\":{\"code\":\"13\",\"name\":\"School of Applied and Professional Psychology, Undergraduate\",\"depts\":[\"047\",\"830\"]},\"14\":{\"code\":\"14\",\"name\":\"School of Engineering\",\"depts\":[\"125\",\"155\",\"180\",\"332\",\"440\",\"540\",\"635\",\"650\"]},\"15\":{\"code\":\"15\",\"name\":\"Graduate School of Education (GSE)\",\"depts\":[\"230\",\"233\",\"245\",\"251\",\"252\",\"253\",\"254\",\"255\",\"256\",\"257\",\"262\",\"267\",\"290\",\"291\",\"293\",\"294\",\"295\",\"297\",\"299\",\"310\"]},\"16\":{\"code\":\"16\",\"name\":\"The Graduate School, New Brunswick\",\"depts\":[\"070\",\"082\",\"107\",\"115\",\"125\",\"137\",\"148\",\"155\",\"160\",\"180\",\"185\",\"190\",\"194\",\"195\",\"198\",\"215\",\"217\",\"218\",\"220\",\"300\",\"332\",\"340\",\"355\",\"356\",\"370\",\"375\",\"378\",\"395\",\"400\",\"420\",\"450\",\"460\",\"470\",\"475\",\"507\",\"510\",\"540\",\"545\",\"550\",\"560\",\"572\",\"615\",\"617\",\"635\",\"640\",\"642\",\"643\",\"650\",\"663\",\"681\",\"682\",\"695\",\"700\",\"709\",\"710\",\"712\",\"718\",\"720\",\"730\",\"731\",\"750\",\"761\",\"762\",\"765\",\"790\",\"830\",\"840\",\"848\",\"910\",\"920\",\"940\",\"954\",\"958\",\"960\",\"963\",\"988\"]},\"17\":{\"code\":\"17\",\"name\":\"School of Communication and Information, Graduate\",\"depts\":[\"194\",\"503\",\"610\"]},\"18\":{\"code\":\"18\",\"name\":\"Graduate School of Applied and Professional Psychology\",\"depts\":[\"820\",\"821\",\"826\",\"829\",\"844\"]},\"19\":{\"code\":\"19\",\"name\":\"School of Social Work, Graduate\",\"depts\":[\"910\"]},\"20\":{\"code\":\"20\",\"name\":\"School of Public Affairs and Administration, Graduate\",\"depts\":[\"831\",\"834\"]},\"21\":{\"code\":\"21\",\"name\":\"College of Arts and Sciences, Newark\",\"depts\":[\"003\",\"014\",\"050\",\"070\",\"074\",\"080\",\"082\",\"083\",\"085\",\"086\",\"087\",\"088\",\"089\",\"090\",\"112\",\"120\",\"160\",\"198\",\"219\",\"220\",\"300\",\"350\",\"352\",\"355\",\"420\",\"460\",\"510\",\"512\",\"525\",\"526\",\"560\",\"580\",\"595\",\"615\",\"640\",\"730\",\"750\",\"790\",\"812\",\"830\",\"910\",\"920\",\"940\",\"988\"]},\"22\":{\"code\":\"22\",\"name\":\"Rutgers Business School, MBA\",\"depts\":[\"010\",\"134\",\"135\",\"140\",\"198\",\"223\",\"373\",\"390\",\"430\",\"544\",\"553\",\"620\",\"621\",\"630\",\"799\",\"835\",\"839\",\"851\",\"960\"]},\"23\":{\"code\":\"23\",\"name\":\"School of Law, Newark\",\"depts\":[\"600\",\"602\"]},\"24\":{\"code\":\"24\",\"name\":\"School of Law, Camden\",\"depts\":[\"601\"]},\"25\":{\"code\":\"25\",\"name\":\"School of Nursing - Undergraduate, Newark\",\"depts\":[\"705\"]},\"26\":{\"code\":\"26\",\"name\":\"The Graduate School, Newark\",\"depts\":[\"010\",\"050\",\"112\",\"120\",\"160\",\"198\",\"200\",\"220\",\"223\",\"300\",\"350\",\"380\",\"390\",\"478\",\"510\",\"553\",\"561\",\"620\",\"630\",\"645\",\"705\",\"711\",\"735\",\"755\",\"790\",\"799\",\"830\",\"834\",\"960\",\"977\",\"988\"]},\"27\":{\"code\":\"27\",\"name\":\"School of Criminal Justice\",\"depts\":[\"202\"]},\"29\":{\"code\":\"29\",\"name\":\"Rutgers Business School, Newark Undergraduate\",\"depts\":[\"010\",\"011\",\"134\",\"382\",\"390\",\"522\",\"620\",\"623\",\"630\",\"799\",\"851\"]},\"30\":{\"code\":\"30\",\"name\":\"Ernest Mario School of Pharmacy, Undergraduate\",\"depts\":[\"158\",\"715\",\"718\",\"720\",\"721\",\"725\"]},\"31\":{\"code\":\"31\",\"name\":\"Ernest Mario School of Pharmacy, Graduate\",\"depts\":[\"720\",\"725\"]},\"33\":{\"code\":\"33\",\"name\":\"Rutgers Business School, New Brunswick Undergraduate\",\"depts\":[\"010\",\"011\",\"136\",\"140\",\"382\",\"390\",\"522\",\"620\",\"630\",\"799\",\"851\"]},\"34\":{\"code\":\"34\",\"name\":\"Bloustein School of Planning and Public Policy, Graduate\",\"depts\":[\"501\",\"816\",\"833\",\"970\"]},\"37\":{\"code\":\"37\",\"name\":\"School of Management and Labor Relations, Undergraduate\",\"depts\":[\"533\",\"575\",\"575;\",\"624\"]},\"38\":{\"code\":\"38\",\"name\":\"School of Management and Labor Relations, Graduate\",\"depts\":[\"533\",\"578\"]},\"40\":{\"code\":\"40\",\"name\":\"School of Public Affairs and Administration, Undergraduate\",\"depts\":[\"834\"]},\"43\":{\"code\":\"43\",\"name\":\"School of Law, Undergraduate\",\"depts\":[\"600\"]},\"47\":{\"code\":\"47\",\"name\":\"School of Criminal Justice, Undergraduate\",\"depts\":[\"202\",\"204\"]},\"50\":{\"code\":\"50\",\"name\":\"College of Arts and Sciences, Camden\",\"depts\":[\"014\",\"070\",\"080\",\"082\",\"115\",\"120\",\"160\",\"163\",\"192\",\"198\",\"202\",\"203\",\"209\",\"220\",\"350\",\"352\",\"354\",\"412\",\"420\",\"443\",\"470\",\"480\",\"499\",\"509\",\"510\",\"512\",\"516\",\"525\",\"570\",\"590\",\"606\",\"609\",\"615\",\"640\",\"698\",\"700\",\"701\",\"730\",\"750\",\"790\",\"830\",\"840\",\"910\",\"920\",\"940\",\"960\",\"965\",\"975\",\"989\"]},\"52\":{\"code\":\"52\",\"name\":\"Camden School of Business, Undergraduate\",\"depts\":[\"010\",\"135\",\"140\",\"390\",\"533\",\"620\",\"623\",\"630\"]},\"53\":{\"code\":\"53\",\"name\":\"Camden School of Business, Graduate\",\"depts\":[\"010\",\"390\",\"620\",\"623\",\"630\",\"716\"]},\"56\":{\"code\":\"56\",\"name\":\"The Graduate School, Camden\",\"depts\":[\"115\",\"120\",\"121\",\"160\",\"163\",\"198\",\"200\",\"202\",\"300\",\"350\",\"412\",\"512\",\"606\",\"615\",\"645\",\"824\",\"830\",\"831\",\"834\",\"842\",\"940\"]},\"57\":{\"code\":\"57\",\"name\":\"School of Nursing - Undergraduate, Camden\",\"depts\":[\"705\"]},\"58\":{\"code\":\"58\",\"name\":\"School of Nursing - Graduate, Camden\",\"depts\":[\"705\"]},\"65\":{\"code\":\"65\",\"name\":\"\",\"depts\":[\"907\"]},\"66\":{\"code\":\"66\",\"name\":\"\",\"depts\":[\"908\"]},\"76\":{\"code\":\"76\",\"name\":\"\",\"depts\":[\"705\"]},\"77\":{\"code\":\"77\",\"name\":\"School of Nursing - Undergraduate, New Brunswick\",\"depts\":[\"705\"]},\"DNTp\":{\"code\":\"DNTp\",\"name\":\"\",\"depts\":[\"PHCO\"]},\"SGS\":{\"code\":\"SGS\",\"name\":\"\",\"depts\":[\"HBSP\",\"RESH\"]},\"SGS;\":{\"code\":\"SGS;\",\"name\":\"\",\"depts\":[\"BIST\",\"ENOH\",\"EPID\",\"HBSP\",\"PHCO\",\"RESH\"]},\"SoN\":{\"code\":\"SoN\",\"name\":\"School of Nursing - Newark\",\"depts\":[\"ADHA\",\"AGAC\",\"AGPC\",\"ANST\",\"CLDR\",\"FENP\",\"MHP\",\"NINF\",\"NMID\",\"NURS\",\"PNP\",\"WHNM\",\"WHNP\"]},\"SPH\":{\"code\":\"SPH\",\"name\":\"School of Public Health\",\"depts\":[\"BIST\",\"ENOH\",\"EPID\",\"HBSP\",\"HOPE\",\"PHCO\",\"UGPH\"]},\"xx\":{\"code\":\"xx\",\"name\":\"\",\"depts\":[\"010\",\"014\",\"090\",\"136\",\"148\",\"160\",\"198\",\"211\",\"293\",\"300\",\"359\",\"440\",\"547\",\"600\",\"640\",\"701\",\"703\",\"709\",\"750\",\"790\",\"834\",\"910\"]}}\n"
val MASTER_SCHOOL_DEPTS_MAP = Json.decodeFromString<Map<String, School>>(b)

//val MASTER_SCHOOL_DEPTS_MAP =
//    mapOf(
//        "01" to School("01","School of Arts and Sciences, New Brunswick",
//            listOf(
//                "013","014","050","070","074","078","082","090","098","119","146","160","165","175","185","190","195","198","202","220",
//                "351","354","355","356","358","359","360","377","381","420","447","450","460","470","489","490","505","506","508","510",
//                "512","556","558","560","563","565","574","580","590","595","615","640","667","685","694","713","723","730","750","787",
//                "790","810","830","840","860","904","920","940","955","960","974","988","991",
//            )
//        ),
//        "03" to School("03","Military Education (ROTC)",
//            listOf(
//                "690","691","692",
//            )
//        ),
//        "04" to School("04","School of Communication and Information, Undergraduate",
//            listOf(
//                "189","192","547","567",
//            )
//        ),
//        "05" to School("05","School of Education (GSE), Undergraduate program",
//            listOf(
//                "300",
//            )
//        ),
//        "07" to School("07","Mason Gross School of the Arts, Undergraduate",
//            listOf(
//                "080","081","203","206","207","211","557","700","701","965","966",
//            )
//        ),
//        "08" to School("08","Mason Gross School of the Arts, Graduate",
//            listOf(
//                "081","208","702","703","966",
//            )
//        ),
//        "09" to School("09","School of Social Work, Undergraduate",
//            listOf(
//                "910",
//            )
//        ),
//        "10" to School("10","Bloustein School of Planning and Public Policy, Undergraduate",
//            listOf(
//                "501","652","762","775","832","833","843","971","975",
//            )
//        ),
//        "11" to School("11","School of Environmental and Biological Sciences (Cook College)",
//            listOf(
//                "015","020","035","067","090","115","117","126","193","216","370","373","374","375","400","550","554","573","607","628",
//                "670","680","709","776",
//            )
//        ),
//        "13" to School("13","School of Applied and Professional Psychology, Undergraduate",
//            listOf(
//                "047","830",
//            )
//        ),
//        "14" to School("14","School of Engineering",
//            listOf(
//                "125","155","180","332","440","540","635","650",
//            )
//        ),
//        "15" to School("15","Graduate School of Education (GSE)",
//            listOf(
//                "230","233","245","251","252","253","254","255","256","257","262","267","290","291","293","294","295","297","299","310",
//            )
//        ),
//        "16" to School("16","The Graduate School, New Brunswick",
//            listOf(
//                "070","082","107","115","125","137","148","155","160","180","185","190","194","195","198","215","217","218","220","300",
//                "332","340","355","356","370","375","378","395","400","420","450","460","470","475","507","510","540","545","550","560",
//                "572","615","617","635","640","642","643","650","663","681","682","695","700","709","710","712","718","720","730","731",
//                "750","761","762","765","790","830","840","848","910","920","940","954","958","960","963","988",
//            )
//        ),
//        "17" to School("17","School of Communication and Information, Graduate",
//            listOf(
//                "194","503","610",
//            )
//        ),
//        "18" to School("18","Graduate School of Applied and Professional Psychology",
//            listOf(
//                "820","821","826","829","844",
//            )
//        ),
//        "19" to School("19","School of Social Work, Graduate",
//            listOf(
//                "910",
//            )
//        ),
//        "20" to School("20","School of Public Affairs and Administration, Graduate",
//            listOf(
//                "831","834",
//            )
//        ),
//        "21" to School("21","College of Arts and Sciences, Newark",
//            listOf(
//                "003","014","050","070","074","080","082","083","085","086","087","088","089","090","112","120","160","198","219","220",
//                "300","350","352","355","420","460","510","512","525","526","560","580","595","615","640","730","750","790","812","830",
//                "910","920","940","988",
//            )
//        ),
//        "22" to School("22","Rutgers Business School, MBA",
//            listOf(
//                "010","134","135","140","198","223","373","390","430","544","553","620","621","630","799","835","839","851","960",
//            )
//        ),
//        "23" to School("23","School of Law, Newark",
//            listOf(
//                "600","602",
//            )
//        ),
//        "24" to School("24","School of Law, Camden",
//            listOf(
//                "601",
//            )
//        ),
//        "25" to School("25","School of Nursing - Undergraduate, Newark",
//            listOf(
//                "705",
//            )
//        ),
//        "26" to School("26","The Graduate School, Newark",
//            listOf(
//                "010","050","112","120","160","198","200","220","223","300","350","380","390","478","510","553","561","620","630","645",
//                "705","711","735","755","790","799","830","834","960","977","988",
//            )
//        ),
//        "27" to School("27","School of Criminal Justice",
//            listOf(
//                "202",
//            )
//        ),
//        "29" to School("29","Rutgers Business School, Newark Undergraduate",
//            listOf(
//                "010","011","134","382","390","522","620","623","630","799","851",
//            )
//        ),
//        "30" to School("30","Ernest Mario School of Pharmacy, Undergraduate",
//            listOf(
//                "158","715","718","720","721","725",
//            )
//        ),
//        "31" to School("31","Ernest Mario School of Pharmacy, Graduate",
//            listOf(
//                "720","725",
//            )
//        ),
//        "33" to School("33","Rutgers Business School, New Brunswick Undergraduate",
//            listOf(
//                "010","011","136","140","382","390","522","620","630","799","851",
//            )
//        ),
//        "34" to School("34","Bloustein School of Planning and Public Policy, Graduate",
//            listOf(
//                "501","816","833","970",
//            )
//        ),
//        "37" to School("37","School of Management and Labor Relations, Undergraduate",
//            listOf(
//                "533","575","575;","624",
//            )
//        ),
//        "38" to School("38","School of Management and Labor Relations, Graduate",
//            listOf(
//                "533","578",
//            )
//        ),
//        "40" to School("40","School of Public Affairs and Administration, Undergraduate",
//            listOf(
//                "834",
//            )
//        ),
//        "43" to School("43","School of Law, Undergraduate",
//            listOf(
//                "600",
//            )
//        ),
//        "47" to School("47","School of Criminal Justice, Undergraduate",
//            listOf(
//                "202","204",
//            )
//        ),
//        "50" to School("50","College of Arts and Sciences, Camden",
//            listOf(
//                "014","070","080","082","115","120","160","163","192","198","202","203","209","220","350","352","354","412","420","443",
//                "470","480","499","509","510","512","516","525","570","590","606","609","615","640","698","700","701","730","750","790",
//                "830","840","910","920","940","960","965","975","989",
//            )
//        ),
//        "52" to School("52","Camden School of Business, Undergraduate",
//            listOf(
//                "010","135","140","390","533","620","623","630",
//            )
//        ),
//        "53" to School("53","Camden School of Business, Graduate",
//            listOf(
//                "010","390","620","623","630","716",
//            )
//        ),
//        "56" to School("56","The Graduate School, Camden",
//            listOf(
//                "115","120","121","160","163","198","200","202","300","350","412","512","606","615","645","824","830","831","834","842",
//                "940",
//            )
//        ),
//        "57" to School("57","School of Nursing - Undergraduate, Camden",
//            listOf(
//                "705",
//            )
//        ),
//        "58" to School("58","School of Nursing - Graduate, Camden",
//            listOf(
//                "705",
//            )
//        ),
//        "65" to School("65","",
//            listOf(
//                "907",
//            )
//        ),
//        "66" to School("66","",
//            listOf(
//                "908",
//            )
//        ),
//        "76" to School("76","",
//            listOf(
//                "705",
//            )
//        ),
//        "77" to School("77","School of Nursing - Undergraduate, New Brunswick",
//            listOf(
//                "705",
//            )
//        ),
//        "DNTp" to School("DNTp","",
//            listOf(
//                "PHCO",
//            )
//        ),
//        "SGS" to School("SGS","",
//            listOf(
//                "HBSP","RESH",
//            )
//        ),
//        "SGS;" to School("SGS;","",
//            listOf(
//                "BIST","ENOH","EPID","HBSP","PHCO","RESH",
//            )
//        ),
//        "SoN" to School("SoN","School of Nursing - Newark",
//            listOf(
//                "ADHA","AGAC","AGPC","ANST","CLDR","FENP","MHP","NINF","NMID","NURS","PNP","WHNM","WHNP",
//            )
//        ),
//        "SPH" to School("SPH","School of Public Health",
//            listOf(
//                "BIST","ENOH","EPID","HBSP","HOPE","PHCO","UGPH",
//            )
//        ),
//        "xx" to School("xx","",
//            listOf(
//                "010","014","090","136","148","160","198","211","293","300","359","440","547","600","640","701","703","709","750","790",
//                "834","910",
//            )
//        )
//    )

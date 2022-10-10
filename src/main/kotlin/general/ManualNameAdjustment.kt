package general

// for typos, bad/inconsistent formatting, and more!
fun manualNameAdjustment(prof: String, code: String): String {
    return when (code.split(":").take(2).joinToString(":")) {
        "01:013" -> when (prof) {
            "KHAYYAT, EFE", "E, EFE", "EFE, E" -> "KHAYYAT, EMRAH" // 01:090, 01:195
            "YKHAYYAT" -> "KHAYYAT, YASMINE"
            else -> prof
        }

        "01:014" -> when (prof) {
            "AMARAL, MAMARAL" -> "AMARAL, MELISSA" // probably
            "CADENAJ, JESENIA" -> "CADENA, JESENIA"
            "JACKSONBREWER, KARLA" -> "JACKSON-BREWER, KARLA"
            "PRICE, MELANYE" -> "PRICE, MELANIE"
            "RAMACHANDRANA, ANITHA", "RAMACHANDRAN, KAVITHA" -> "RAMACHANDRAN, ANITHA" // 2nd probably a typo
            "WALTON, JOHNSON" -> "JOHNSON, WALTON"
            "WHITNEYIII", "WHITNEYIII, JAMES", "WHITNEY, JANES", "WHITNEY, JAMES" -> "WHITNEY III, JAMES"
            else -> prof
        }

        "01:050" -> when (prof) {
            "ALONSO, BEJARANO" -> "ALONSO, CAROLINA" // 01:595
            "LINDHREEVES, ELIZABETH" -> "REEVES, ELIZABETH"
            "MARTINEZ-SAN, MIGUEL", "MARTINEZ, E" -> "MARTINEZ-SAN, YOLANDA" // 2nd probably / 01:195, 01:595
            "SIFUENTES, BEN" -> "SIFUENTES-JAUREGUI, BEN" // 01:090
            else -> prof
        }

        "01:070" -> when (prof) {
            "CABANES, CRUELLES" -> "CABANES, DAN"
            "GHASSEM-FACHAN", "GHASSEM-FACHAN, PARVIS" -> "GHASSEM-FACHANDI, PARVIS"
            else -> prof
        }

        "01:078" -> when (prof) {
            "VASILIAN, ASBED" -> "VASSILIAN, ASBED"
            else -> prof
        }

        "01:082" -> when (prof) {
            "MIEKE, PAULSEN" -> "PAULSEN, MIEKE"
            "PORTSNER, LAURIE" -> "PORSTNER, LAURIE"
            "WOODHOUSEBEYER, KATHERINE", "WOODHOUSEBEYER, KATHARI" -> "WOODHOUSE-BEYER, KATHARINE"
            else -> prof
        }

        "01:090" -> when (prof) {
            "ALEXANDERFLOYD, NIKOL", "ALEXANDER, FLOYD", "ALEXANDER, NIKOL" -> "ALEXANDER-FLOYD, NIKOL" // 01:988
            "ANDROULAKIS, YANNIS", "ANDROULAKIS, IONNIS" -> "ANDROULAKIS, IOANNIS"
            "BAIER, BECCA" -> "BAIER, REBECCA"
            "BATHORY, D" -> "BATHORY, PETER" // probably
            "BEAL, MIKE" -> "BEALS, MIKE"
            "BERKELEY, SHORNA" -> "BERKELEY, SHORNNA"
            "BERKELY, S" -> "BERKELEY, SHORNNA"
            "BORRMAN-BEGG, CAROL" -> "BEGG, CAROL"
            "BRENNAN, C" -> "BRENNAN, T"  // probably
            "CAHILL, C" -> "CAHILL, KATE" // probably
            "DALBELLO, MARISA" -> "DALBELLO, MARIJA"
            "DEBORAH, AKS" -> "AKS, DEBORAH"
            "EFE, E" -> "EFE, KHAYYAT"  // 01:013, 01:195
            "GADSDEN, PETAL" -> "BRITTON-GADSDEN, PETAL"
            "GHASSEM-FACHAN" -> "GHASSEM-FACHANDI, PARVIS"
            "GONZALEZ, BARBARA", "GONZALEZ, PALMER" -> "GONZALEZ-PALMER, BARBARA"
            "GORDON, RAHJUAN" -> "GORDON, RAHJAUN"
            "HAGHANI, FAKHROLMOL" -> "HAGHANI, FAKHRI" // probably / 01:685, 01:988
            "HEUMANN, MILTOW" -> "HEUMANN, MILTON"
            "KAMINISKI, LAURA" -> "KAMINSKI, LAURA"
            "KAPTAN, ALLEN" -> "KAPTAN, SENEM"
            "KAUFMAN, TRICIA" -> "KAUFMAN, TRISHA"
            "KOSINKI, KIMBERLY" -> "KOSINSKI, KIMBERLY"
            "KULIKOWSKI, CHARLES" -> "KULIKOWSKI, CASIMIR" // probably
            "LARANJEIRO, JOSE" -> "LARANJEIRO, JOE"
            "LENAHAN, CLEARY" -> "LENAHAN, JENNIFER"
            "LEWIS, FRANKIE" -> "LEWIS, FRANCIS" // probably
            "LOPEZ, KATHY" -> "LOPEZ, KATHERINE" // probably / 01:508
            "LORD, N" -> "LORD, MUFFIN" // probably
            "MANDELBAUM, JENNY" -> "MANDELBAUM, JENNIFER"
            "MARKOWITZ, NORMAM" -> "MARKOWITZ, NORMAN"
            "MARTINEZSANMIGUEL, YOLANDA", "MARTINEZSAN, YOLANDA" -> "MIGUEL, YOLANDA" // shortenedd the name
            "MCCROSSIN, TRIP", "MCCROSSIN, T", "MCCROSSIN, EDWAD" -> "MCCROSSIN, EDWARD" // 01:730
            "MCGREWC, CHARLES" -> "MCGREW, CHARLES"
            "MONTELIONE, GUY" -> "MONTELIONE, GAETANO"
            "NAZARIO, JUILO", "NAZARI, JULIO", "NAZARIO, I" -> "NAZARIO, JULIO"
            "OTEROTORRES, DAMARIUS" -> "OTEROTORRES, DAMARIS"
            "RAMIRIZ, CATHERINE", "RAMERIZ, CATHERINE" -> "RAMIREZ, CATHERINE" // probably
            "REARDON, ABBIE" -> "REARDON, ABIGAIL"
            "RITTER, JUCIA", "JULIA, RITTER" -> "RITTER, JULIA"
            "RUSSELLJONES, SANDRA" -> "RUSSELL-JONES, SANDRA"
            "SALZMAN, HAROLD" -> "SALZMAN, HAL" // I think?
            "SCHNETZER, STEVE" -> "SCHNETZER, STEPHEN"
            "SCOTT, KATHY" -> "SCOTT, KATHLEEN"
            "SIFUENTESJAUREGU, BEN", "SIFUENTES, BEN" -> "SIFUENTES-JAUREGU, BEN" // 01:050
            "SIMMONS, RV" -> "SIMMONS, RICHARD" // probably
            "SMITH, DR" -> "SMITH, RANDALL" // probably
            "SPEAR, BILL" -> "SPEAR, WILLIAM" // probably
            "SPERLING, ALI" -> "SPERLING, ALESSANDRA"
            "ST, GEORGE" -> "ST. GEORGE, MICHELLE"
            "SUTTON, TOPHER" -> "SUTTON, CHRISTOPHER"
            "WENZEL, JACK" -> "WENZEL, JOHN"
            "WILSON, JENNIFER" -> "GIBSON, JENNIFER"
            "WUPALUMBO, CONNIELAUR" -> "WU, CONNIE" // other is PALUMBO, LAURA
            else -> prof
        }

        "01:119" -> when (prof) {
            "GLODOWSKI, TROTTA" -> "GLODOWSKI, DOREEN" // 01:447
            else -> prof
        }

        "01:146" -> when (prof) {
            "CARR-SCHMID, A" -> "CARR-SCHMID, ELEANOR"
            "FIRESTEIN-MILLER, BONNIE" -> "FIRESTEIN, BONNIE"
            "GOLFETI, ROSELI" -> "GOLFETTI, ROSELI"
            "WU, LONGSUN", "LONG, JUNWU" -> "WU, LONG-JUN"
            else -> prof
        }

        "01:160" -> when (prof) {
            "ALTINIS, CHRISTINE" -> "ALTINIS-KIRAZ, CHRISTINE"
            "ARNOLD, EDDY" -> "ARNOLD, EDWARD"
            "ASHWINI, RANADE" -> "RANADE, ASHWINI"
            "BILLMERS, BOB", "BILLMERS, BILLMERS" -> "BILLMERS, ROBERT"
            "BOIKESS, BOB" -> "BOIKESS, ROBERT"
            "CHEN, KY" -> "CHEN, KUANG-YU"
            "DISMUKES, GERARD" -> "DISMUKES, CHARLES" // probably
            "HINCH, BARBARA" -> "HINCH, JANE"
            "HYUNJIN, KIM" -> "KIM, HYUNJIN"
            "JIMINEZ, LESLIE" -> "JIMENEZ, LESLIE"
            "KROGH-JESPERSE, KARSTEN" -> "KROGH-JESPERSEN, KARSTEN"
            "MARCOTRIGIANO, JOESEPH" -> "MARCOTRIGIANO, JOSEPH"
            "MARVASTI, SATAREH" -> "MARVASTI, SETAREH"
            "OLSON, WILIMA" -> "OLSON, WILMA"
            "PRAMINIK, SANHITA" -> "PRAMANIK, SANHITA"
            "RABEONY, MANSES" -> "RABEONY, MANESE"
            "ROMSTED, LAWRENCE" -> "ROMSTED, LAURENCE"
            "ROYCHOWDUHURY, LIPIKA", "ROYCHOWDURY, LIPIKA" -> "ROYCHOWDHURY, LIPIKA"
            "SHANKAR, NIRMILA" -> "SHANKAR, NIRMALA"
            "SOUNDARAJAN, NACHIMUTHU" -> "SOUNDARARAJAN, NACHIMUTHU"
            "WOOSEOK, KI" -> "KI, WOOSEOK"
            "YORK, DARREN" -> "YORK, DARRIN"
            else -> prof
        }

        "01:175" -> when (prof) {
            "FLITTERMANLEWIS, SANDY" -> "FLITTERMAN-LEWIS, SANDRA" // 01:354
            "NIGRIN, ACBERT" -> "NIGRIN, ALBERT"
            else -> prof
        }

        "01:190" -> when (prof) {
            "ALLENHORNBLOWE, EMILY" -> "ALLEN-HORNBLOWER, EMILY"
            "FISHER, JAY" -> "FISHER, JOHN" // 01:490, 01:580, 01:615 / confident about this
            else -> prof
        }

        "01:195" -> when (prof) {
            "FANELLI, LAUREN" -> "FANELLI-TEAGUE, LAUREN" // 01:355
            "KHYYAT, EMRAH", "KHAYYAT, EFE", "EFE, E" -> "KHAYYAT, EMRAH" // 01:013, 01:090
            "KITZINGER, CHLOE", "KITZINGER, C" -> "KITZINGER-SHEDLOCK, CHLOE"
            "LIDIA, LEVKOVITCH" -> "LEVKOVITCH, LIDIA"
            "MARTINEZ-SAN, MIGUEL", "MARTINEZ, E" -> "MARTINEZ-SAN, YOLANDA" // 2nd probably / 01:050, 01:595
            "SCLAFANI, KATHY", "KATHLEEN, SCLAFANI" -> "SCLAFANI, KATHLEEN"
            "STEVEN, GONZAGOWSKI" -> "GONZAGOWSKI, STEVEN"
            "YU-I, HSIEH" -> "HSIEH, YU-I"
            else -> prof
        }

        "01:198" -> when (prof) {
            "CHIRCO, JT", "CHRICO, J" -> "CHIRCO, JOHN"
            "DESSAI, ANAGHA" -> "DESAI, ANAGHA" // probably
            "GUNAWARDENA, ANDY" -> "GUNAWARDENA, ANANDA"
            "LMIELINSKI, T" -> "IMIELINSKI, TOMASZ" // 01:960
            "MIRANDA, GARCIA" -> "MIRANDA, ANTONIO"
            "NARAYANA, GANAPATHY" -> "NARAYANA, SRINIVAS"
            "RAMAKRISHNA, R" -> "RAMAKRISHNAN, REMYA" // probably
            "SHETTY, KARTIK" -> "SHETTY, KARTHIK"
            "SRIVASTAVA, PRAHKAR" -> "SRIVASTAVA, PRAKHAR"
            "WEIDENHOFT, J" -> "WIEDENHOEFT, JOHN"
            "ZHIQIANG, TANG" -> "TANG, ZHIQIANG"
            else -> prof
        }

        "01:202" -> when (prof) {
            "KOHL, JAY" -> "KOHL, JAMES" // I think
            else -> prof
        }

        "01:220" -> when (prof) {
            "CARBONELL-N, O", "CARBONELL, ORIOL" -> "CARBONELL-NICOLAU, ORIOL"
            "CROCKETT, BARBARA" -> "CROCKETT, ERIN"
            "NOSRATABADI, S" -> "NOSRATABADI, HASSAN"
            "SJOSTROM, TOMAS", "SJOSTROM, T" -> "SJOSTROM, JOHN"
            "TORRES-RENYA, OSCAR" -> "TORRES-REYNA, OSCAR"
            else -> prof
        }

        "01:351" -> when (prof) {
            "WIRSTIUK, LYRYSSA" -> "WIRSTIUK, LARYSSA"
            "RZIGALINKSKI, CHRISTOPHER" -> "RZIGALINSKI, CHRISTOPHER"
            "HOBOYAN, LESLIEANN" -> "HOBAYAN, LESLIEANN"
            "PEARLSTEIN, RANDY" -> "PEARLSTEIN, RANDALL"
            "KLAVER, BECCA" -> "KLAVER, REBECCA"
            "NIKOLOPOULOS, EVAGELOS" -> "NIKOLOPOULOS, ANGELO"
            "WALLISHUGHES, EMILY" -> "HUGHES, EMILY"
            else -> prof
        }

        "01:354" -> when (prof) {
            "SAVERINO, ANASTASIA" -> "SAVERINO, ANASTASAA"
            "FLITTERMANLEW, SANDY", "FLITTERMANLEWI, SANDY", "FLITTERMANLEWIS, SANDY" -> "FLITTERMAN-LEWIS, SANDRA"
            else -> prof
        }

        "01:355" -> when (prof) {
            "BASS, JQNATHAN" -> "BASS, JONATHAN"
            "BORIEHOLTZ, DEBBIE" -> "BORIEHOLTZ, DEBRA"
            "BOUTIN, CLAUDE", "BOUTIN, HARRYCLAUDE" -> "BOUTIN, HARRY"
            "BRIE, ASHLEY" -> "ASHLEY, BRIE"
            "CANTOR, NICOLE" -> "COHEN, NICOLE" // probably typo
            "CHOWDHURY, NANDINCH" -> "CHOWDHURY, NANDINI"
            "COHAN, DARCY" -> "GIOIA, DARCY" // probably typo
            "DAZA, VANESSA" -> "DAZA-HECK, VANESSA"
            "DUFFY, MIKE" -> "DUFFY, MICHAEL"
            "HARUKI, EDA" -> "EDA, HARUKI"
            "FANELLI, LAUREN" -> "FANELLI-TEAGUE, LAUREN" // 01:195
            "FOLEM, SEAN" -> "FOLEY, SEAN"
            "GILMARTIN, VGILMAR" -> "GILMARTIN, VIRGINIA"
            "GOELLER, ANGIESZKA" -> "GOELLER, AGNIESZKA"
            "HAMLET, BMENDA" -> "HAMLET, BRENDA"
            "JAVONSKI, JORDANCO" -> "JOVANOSKI, JORDANCO"
            "LIBBY, PHILIP", "LIBBY, PHILLIP", "LIBBY, PHILIPANDR", "LIBBY, PHILIPANDREW" -> "LIBBY, ANDREW"
            "NACHESCU, VOLCHITA" -> "NACHESCU, VOICHITA"
            "NELSON, BOB" -> "NELSON, ROBERT"
            "NEVINS, JESSICA" -> "NEVIN, JESSICA"
            "OLTARZEWSKI, MJ", "OLTARZEWSKI, A" -> "OLTARZEWSKI, MARYJANE" // 2nd probably
            "PERSSON, TORLIEF", "PERSSON, TORLEIF" -> "PERSSON, BO"
            "ROBINSON, MIXON" -> "ROBINSON, RALEIGH"
            "SANDBERG, GOLDEN", "SANDBERG, LENA", "GOLDENSANDBERG, LENA" -> "SANDBERG-GOLDEN, LENA"
            "SAVILLE, DUDLEYALEX", "SAVILLE, ALEX" -> "SAVILLE, DUDLEY"
            "SCHMID, LETITIA" -> "SCHMID, LETIZIA"
            "SCHROEPHER, GEORGE" -> "SCHROEPFER, GEORGE"
            "TUCKSON, NINA" -> "TUCKSON, NIA"
            "WILFORD, KATHY" -> "WILFORD, KATHLEEN"
            "ZIEBA, IZABELLA" -> "ZIEBA, IZABELA" // not sure which one is correct tbh
            else -> prof
        }

        "01:356" -> when (prof) {
            "CAPONEGRO, MJHAELA" -> "CAPONEGRO, MIHAELA"
            "CUSUMANO, RICH" -> "CUSUMANO, RICHARD"
            "CUSUMANO, ROSE" -> "CUSUMANO, ROSEMARIE" // would be auto-fixed if they didn't have the same last name!
            "MATHEWS, CHISTIAN" -> "MATHEWS, CHRISTIAN"
            "NAVARRO, NELA", "NAVARRO, NECA", "NAVARRON, NELA", "NAVARROLAPOINT, NELA" -> "NAVARRO-LAPOINTE, NELA"
            "NACHESCUV, VOICHITA" -> "NACHESCU, VOICHITA"
            "SCHNATTER, KERISTIN" -> "SCHNATTER, KERSTIN"
            "SLOVICK, SHARRON" -> "SLOVICK, SHARON"
            "TANNER, AARON" -> "TANNER, WILLIAM"
            else -> prof
        }

        "01:358" -> when (prof) {
            "BUCKLEY, MATHEW" -> "BUCKLEY, MATTHEW" //left off here - did not check this one
            "CAMRADA, JULIE", "CAMARADA, JULIE" -> "CAMARDA, JULIE" // this name isn't present but seems to be right
            "DIENSTR, RICHARD" -> "DIENST, RICHARD"
            "IBIRONKE, BODE" -> "IBIRONKE, OLABODE"
            "LAWRENCE, JEFFERY" -> "LAWRENCE, JEFFREY"
            "MANGHARAN, MUKTI" -> "MANGHARAM, MUKTI"
            "ROBOLIN, STEPHANIE" -> "ROBOLIN, STEPHANE"
            "SCANLON, LARRY" -> "SCANLON, LAWRENCE"
            "WALL, CWALL" -> "WALL, CHERYL" // probably
            else -> prof
        }

        "01:359" -> when (prof) {
            "EDIAMOND, ELIN" -> "DIAMOND, ELIN"
            "GROGAN, KRIRTIN" -> "GROGAN, KRISTIN"
            "GUZZARDOTAMARG, ISABEL", "GUZZARDOTAMARGO" -> "TAMARGO, ISABEL"
            "IBIRONKE, BODE" -> "IBIRONKE, OLABODE" // 01:358
            "MATHES, CARTHER" -> "MATHES, CARTER"
            "PERSSON, TORLEIF" -> "PERSSON, BO" // 01:355
            else -> prof
        }

        "01:360" -> when (prof) {
            "PETURRSON, SVANUR" -> "PETURSSON, SVANUR" // 01:790
            else -> prof
        }

        "01:377" -> when (prof) {
            "COSLOY, JAIME", "COSLOY, J" -> "HECHT-COSLOY, JAIME" // 01:955
            "DANDREA, CHRISTOPHER", "CHRISTOPHER, DANDREA" -> "D'ANDREA, CHRISTOPHER"
            "GLADIS, KATHI" -> "GLADIS, KATHLEEN"
            "JOANNE, HUNT" -> "HUNT, JOANNE"
            "MYRON, FINKELSTEIN" -> "FINKELSTEIN, MYRON"
            "NANCY, GOLDBERG" -> "GOLDBERG, NANCY"
            "ROBELL, NAGLE" -> "ROBELL, NICOLE"
            else -> prof
        }

        "01:420" -> when (prof) {
            "BRANTON-DESRIS, JENNIFER" -> "BRANTON-DESRIS, JENIFER"
            "KARMARMAR", "KARMAKAR, MEDHA" -> "KARMARKAR, MEDHA"
            "PAIRET, VINAS" -> "PAIRET, ANA"
            else -> prof
        }

        "01:447" -> when (prof) {
            "GLODOWSKI, TROTTA" -> "GLODOWSKI, DOREEN" // 01:119
            else -> prof
        }

        "01:450" -> when (prof) {
            "GHERTNER, DAVID", "GHERTNER, D" -> "GHERTNER, ASHER"
            else -> prof
        }

        "01:460" -> when (prof) {
            "AUBRY, M-P" -> "AUBRY, MARIE-PIERRE"
            "DELANEY, JERRY" -> "DELANEY, JEREMY" // probably
            "HERZBERG, G" -> "HERZBERG, CLAUDE" // typo probably
            "MORTLOCK, RICK" -> "MORTLOCK, RICHARD"
            "PETER, SUGARMAN" -> "SUGARMAN, PETER"
            "SEVERMAN, SILKE" -> "SEVERMANN, SILKE"
            "SEQUEIRA, PERAZA" -> "SEQUEIRA, CESAR"
            "TIKOO, SONIA" -> "TIKOO-SCHANTZ, SONIA"
            "VANTOGEREN, JILL" -> "VANTONGEREN, JILL"
            else -> prof
        }

        "01:489" -> when (prof) {
            "KOURTIGAVALAS, KATHERINE" -> "KOURTI, KATHERINE"
            else -> prof
        }

        "01:490" -> when (prof) {
            "FISHER, JAY" -> "FISHER, JOHN" // 01:190, 01:580, 01:615
            else -> prof
        }

        "01:506" -> when (prof) {
            "BELLRODEN, RUDOLPHDON" -> "BELL, RUDY"
            "LAURIA-SANTIAG, ALDO" -> "LAURIA, ALDO"
            "LIVINGSTON, JIM" -> "LIVINGSTON, JAMES"
            "O'BRASSILL-KUL, KRISTIN" -> "O'BRASSILL-KULFAN, KRISTIN"
            "WOODHOUSEBEYER, KATHERINE" -> "WOODHOUSE-BEYER, KATHARINE"
            else -> prof
        }

        "01:508" -> when (prof) {
            "LAURIA, SANTIAGO" -> "LAURIA-SANTIAGO, ALDO" // 01:512, 01:595
            "LOPEZ, KATHY" -> "LOPEZ, KATHLEEN" // probably
            "RATZMAN, ELI" -> "RATZMAN, ELLIOT" // 01:563, 01:685
            "RUSSELL-JONES, SANDY", "RUSSELL, SANDRA" -> "RUSSELL-JONES, SANDRA" // 01:090, 01:685
            else -> prof
        }

        "01:510" -> when (prof) {
            "DIBATTISTA, ANTONY" -> "DIBATTISTA, ANTHONY"
            "FIGUEIRA, TOM" -> "FIGUEIRA, THOMAS"
            "REINERT, STEVE" -> "REINERT, STEPHEN"
            else -> prof
        }

        "01:512" -> when (prof) {
            "LAURIA, SANTIAGO", "LAURIA, ALDO" -> "LAURIA-SANTIAGO, ALDO" // 01:508, 01:595
            "LEE, KATHERINE" -> "LEE, KATHARINE"
            "MARKOWITZ, WORMAN" -> "MARKOWITZ, NORMAN" // 01:090
            "OBRASSILLKULF, KRISTIN" -> "O'BRASSILL-KULFAN, KRISTIN" // 01:506
            else -> prof
        }

        "01:556" -> when (prof) {
            "WHITNEY, JAMES", "WHITNEY, J" -> "WHITNEY III, JAMES"
            else -> prof
        }

        "01:560" -> when (prof) {
            "CARMELA, SCALA" -> "SCALA, CARMELA"
            "DONATA, PANIZZA" -> "PANIZZA, DONATA"
            "FOGNANI, ARIANA" -> "FOGNANI, ARIANNA"
            "HIROMI, KANEDA" -> "KANEDA, HIROMI"
            "ILONA, HRENKO" -> "HRENKO, ILONA"
            "TIMOTHY, CURCIO" -> "CURCIO, TIMOTHY"
            "TIZIANO, CHERUBINI" -> "CHERUBINI, TIZIANO"
            else -> prof
        }

        "01:563" -> when (prof) {
            "RATZMAN, ELI" -> "RATZMAN, ELLIOT" // 01:508, 01:685
            "SHANDLER, JEFFERY" -> "SHANDLER, JEFFREY"
            else -> prof
        }

        "01:565" -> when (prof) {
            "FLEMING, NATUSKO", "FLEMING, NATSUKO" -> "BUURSTRA, NATSUKO" // got married seemingly
            "OKADA-HAWALKA, YASUKO" -> "HAWALKA, YASUKO"
            "PIOTROWSKI, MIKKO", "PIOTROWSKI, A" -> "PIOTROWSKI, MIKIKO" // 2nd probably
            "S, MAYNARD" -> "MAYNARD, SENKO"
            else -> prof
        }

        "01:574" -> when (prof) {
            "CHUN, HEE" -> "CHUNG, JAE"
            "MEDINA, JENNIFER", "MEDINA, JENNY", "WANG, MEDINA" -> "WANG-MEDINA, JENNY"
            "PARK, SUNMIN" -> "PARK, SUNGMIN"
            else -> prof
        }

        "01:580" -> when (prof) {
            "FISHER, JAY" -> "FISHER, JOHN" // 01:190, 01:490, 01:615
            else -> prof
        }

        "01:590" -> when (prof) {
            "ROCHA, GIESA" -> "ROCHA, GEISA"
            "STEPHENS, TOM" -> "STEPHENS, THOMAS"
            else -> prof
        }

        "01:595" -> when (prof) {
            "ALONSO, BEJARANO" -> "ALONSO, CAROLINA" // 01:050
            "C, FERNANDEZ" -> "FERNANDEZ, CARLOS"
            "DINZEY-FLORES, ZAIRE", "DINZEY-FLORES, Z", "DINZEY-FLORES" -> "DINZEY, ZAIRE" // 01:920
            "DUCHESNE" -> "DUCHESNE-SOTOMAYOR, DAFNE"
            "FIGUEROA, YOIMARA" -> "FIGUEROA, YOMAIRA"
            "GARCIA, WILLIAM" -> "GARCIA-MEDINA, WILLIAM"
            "LAURIA, SANTIAG", "LAURIA, SANTIAGO", "LAURIA, ALDO", "LAURIA-SANTIAG, ALDO", "LAURIA-SANTIAG, A", "LAURIA-SANTIAG" -> "LAURIA-SANTIAGO, ALDO"
            "LOPEZ, KATHY" -> "LOPEZ, KATHLEEN" // probably / 01:090
            "MALDONADO-TORR, NELSON", "MALDONADO, TORRES" -> "MALDONADO-TORRES, NELSON"
            "MARTINEZ-SAN, MIGUEL", "MARTINEZ, E" -> "MARTINEZ-SAN, YOLANDA" // 2nd probably / 01:050, 01:195
            "STEPHENS, M" -> "STEPHENS, THOMAS"
            else -> prof
        }

        "01:615" -> when (prof) {
            "DELACY, PAUL" -> "DE LACY, PAUL"
            "FISHER, JAY" -> "FISHER, JOHN" // 01:190, 01:490, 01:580, 01:615
            "HOUGHTEN, P" -> "HOUGHTON, PAULA" // probably
            "SYRETT, KRISTIN" -> "SYRETT, KRISTEN"
            else -> prof
        }

        "01:640" -> when (prof) {
            "NUNZIANTE, DIANA", "BAHRI, NUNZIANTE" -> "BAHRI-NUNZIANTE, DIANA"
            "BALASUBRAMANIA, MOULIK", "KALLUPALAM, BALASUBRAMANIAN", "KALLUPALAM, BALASUBRAM" -> "BALASUBRAM, MOULIK"
            "BARRETO, V" -> "BARRETO-ARANDA, VICTOR"
            "BEALS, MICHAEL" -> "BEALS, ROBERT"
            "COHEN, AMY" -> "COHEN-CORWIN, AMY"
            "ECHEVERRIA, ECHEVERRIA" -> "ECHEVERRIA, MARIANO"
            "EVANS, JUDY" -> "EVANS, JUDITH"
            "FINKLESTEIN, JOSHUA" -> "FINKELSTEIN, JOSHUA"
            "FISHER, RANDY" -> "FISHER, RANDOLPH"
            "GAMEIRO-, FUZETO" -> "GAMEIRO-FUZETO, MARCIO"
            "GAMOVA, H" -> "GAMOVA, ELENA"
            "GINDIKIN, SEMEN" -> "GINDIKIN, SIMON"
            "HUANG, XUIAOJUN" -> "HUANG, XIAOJUN"
            "MUKHERJEE, ARJUN" -> "MUKHERJEE, ARUN"
            "PATEL, DAN", "PATEL, DHAN" -> "PATEL, DHANSUKH"
            "SANCHEZ, TAPIA" -> "SANCHEZ-TAPIA, CYNTHIA"
            "SPRAGUE, GABRIELLE" -> "SPRAGUE, GABRIELA"
            "SUSSMAN, HECTOR" -> "SUSSMANN, HECTOR"
            "THELFALL, SHAWN", "THRELLFALL, SHAWN" -> "THRELFALL, SHAWN"
            "TRALDI, -", "TRALDI, ELIANE", "ZERBETTO, -" -> "ZERBETTO, ELIANE"
            else -> prof
        }

        "01:667" -> when (prof) {
            "DIBATTISTA, ANTONY", "DI, BATTISTA" -> "DIBATTISTA, ANTHONY"
            else -> prof
        }

        "01:685" -> when (prof) {
            "ABDELJABER, ABDELHAMID" -> "ABDELJABER, HAMID"
            "HAGHANI, FAKHRI" -> "HAGHANI, FAKHROLMOLOUK" // 01:090, 01:988
            "KHAYYAT, EFE", "EFE, E" -> "KHAYYAT, EMRAH" // 01:013, 01:090, 01:195
            "RATZMAN, ELI" -> "RATZMAN, ELLIOT" // 01:508, 01:563
            "RUSSELL-JONES, SANDY", "RUSSELL, SANDRA", "RUSSELL, JONES" -> "RUSSELL-JONES, SANDRA" // 508
            "WEIRECH" -> "WEIRICH"
            else -> prof
        }

        "01:694" -> when (prof) {
            "EDERY, ISSAC" -> "EDERY, ISAAC"
            "GU, S" -> "GU, GUOPING" // probably
            "MEAD, PARENT" -> "MEAD, JANET"
            "PADGET, R" -> "PADGETT, RICHARD"
            "ZARATIEGUI, BIURRUN" -> "ZARATIEQUI, MIGUEL"
            else -> prof
        }

        "01:730" -> when (prof) {
            "CHERYL, BRADEN" -> "BRADEN, CHERYL"
            "DI, SUMMA-KNOOP", "DISUMMA-KNOOP, L" -> "DI SUMMA-KNOOP, LAURA"
            "EGAN, MARY" -> "EGAN, FRANCES"
            "KALEF, JUSTIN", "KALEF, J" -> "KALEF, PETER"
            "KANG, SUNG" -> "KANG, STEVEN"
            "LEPORE, ERNIE" -> "LEPORE, ERNEST"
            "MARCELLO, ANTOSH" -> "ANTOSH, MARCELLO"
            "MCCELLION, T" -> "MCCELLION, LAVARIS" // probabl
            "MCCROSSIN, TRIP", "MCCROSSIN, T", "MCCROSSIN, EDWAD" -> "MCCROSSIN, EDWARD" // 01:090
            "RATZMAN, ELI" -> "RATZMAN, ELLIOT" // other places
            "SIDER, TED" -> "SIDER, THEODORE"
            else -> prof
        }

        "01:750" -> when (prof) {
            "CHANT, B" -> "CHANT, ROBERT"
            "CHEONG, S-W" -> "CHEONG, SANG-WOOK"
            "CHOU, JP" -> "CHOU, JOHN"
            "GERHSTEIN, Y" -> "GERSHTEIN, YURI"
            "HARMON, S" -> "HARMAN, S"
            "KIRYUKIN, VALERY", "KIRKYUKHIN, V" -> "KIRYUKHIN, VALERY"
            "LEE, SH" -> "LEE, SANG-HYUK"
            "MALLIARIS, CONSTANTIN" -> "MALLIARIS, TED"
            "PRYOR, T" -> "PRYOR, CARLTON"
            "ZIMMERMAN, F", "ZIMMERMAN" -> "ZIMMERMANN, FRANK"
            else -> prof
        }

        "01:790" -> when (prof) {
            "ABDELJABER, ABDELHAMID" -> "ABDELJABER, HAMID"
            "BATHORY, DENNIS" -> "BATHORY, PETER"
            "BIZZOCO, NIKKI", "BIZOCCO" -> "BIZZOCO, NICOLE"
            "CARNEY-WATERTO", "WATERTON, JO-LEO" -> "CARNEY-WATERTON, JO-LEO"
            "HARRISON, EWAN" -> "HARRISON, RICHARD"
            "KELEMEN, DAN", "KELEMEN, D" -> "KELEMEN, ROGER"
            "MIDLARSKI" -> "MIDLARSKY, MANUS"
            "PETURRSON, SVANUR" -> "PETURSSON, SVANUR" // 01:360
            "PRICE, MELAYNE" -> "PRICE, MELANYE"
            "RESTREPO, SANIN" -> "SANIN, JULIANA"
            "ROSSI, MIKE" -> "ROSSI, MICHAEL"
            "SOCHA, BAILEY" -> "EAISE, BAILEY" // married I think
            "TERENCE, TEO" -> "TEO, TERENCE"
            "X, HUANG" -> "HUANG, XIAN"
            else -> prof
        }

        "01:810" -> when (prof) {
            "TAMANAHA, DAIANE" -> "TAMANAHA DE QUADROS, DAIANE"
            else -> prof
        }

        "01:830" -> when (prof) {
            "BOYCE, JACINO" -> "BOYCE-JACINO, C"
            "CHANG, A" -> "CHANG, QING"
            "LYRA, STEIN" -> "STEIN, LYRA"
            "NICOLAS, FERREIRA" -> "NICOLAS, GANDALF"
            "NORTAN, E" -> "NORTAN, S"
            else -> prof
        }

        "01:840" -> when (prof) {
            "BALLENTINE, DEBSA" -> "BALLENTINE, DEBRA"
            "BISHOP, KAHTLEEN" -> "BISHOP, KATHLEEN"
            "LAMMERTS, CHRISTIAN", "LAMMERTS, LAMMERTS", "LAMMERTS MIKAYLA" -> "LAMMERTS, DIETRICH" // 3rd probably
            "RUSSELL, JONES", "RUSSELL, SANDRA", "RUSSELLJONES, SANDRA" -> "RUSSEL-JONES, SANDRA"
            "SUROWITZISRAEL, HILIT", "SUROWITZ, ISRAEL" -> "SUROWITZ, HILIT"
            else -> prof
        }

        "01:860" -> when (prof) {
            "KITZINGER, CHLOE", "KITZINGER, CHLOË" -> "KITZINGER-SHEDLOCK, CHLOE"
            "MEDVEDEVA, NATALIE" -> "MEDVEDEVA, NATALIA"
            else -> prof
        }

        "01:920" -> when (prof) {
            "DINZEY-FLORES, ZAIRE", "DINZEY-FLORES, Z" -> "DINZEY, ZAIRE" // 01:595
            "ELEANOR, LAPOINTE" -> "LAPOINTE, ELEANOR"
            "SMITH, DR" -> "SMITH, DAVID"
            "SONG, K" -> "SONG, EUNKYUNG" // probably
            "WILLHELMS, JEFFREY" -> "WILHELMS, JEFFREY"
            else -> prof
        }

        "01:950" -> when (prof) {
            "COSLOY, J" -> "HECHT-COSLOY, JAIME" // 01:377
            else -> prof
        }

        "01:940" -> when (prof) {
            "ANDREW, VILLADA" -> "VILLADA, ANDREW"
            "BONNIE, BUTLER" -> "BUTLER, BONNIE"
            "CHIVUKULA, DAYCI" -> "CHIVUKULA, LUCRECIA"
            "KAREN, SANCHEZ" -> "SANCHEZ, KAREN"
            "KIM, Y-S" -> "KIM, YEON-SOO"
            "PEREZ, CORTES" -> "PEREZ-CORTES, SILVIA"
            "SANCHEZ, INOFUENTES", "SANCHEZ-INOFUENTES, CELSO", "SANCHEZ-INOFUENTES, CELS", "SANCHEZ-INOFUENTES, C" -> "SANCHEZ, CELSO"
            "STEPHENS, TOM" -> "STEPHENS, THOMAS"
            "VILLALBA, ROSADO" -> "VILLALBA, CELINES"
            else -> prof
        }

        "01:960" -> when (prof) {
            "DEOKI, SHARMA" -> "SHARMA, DEOKI"
            "DONG, HK" -> "DONG, HEI-KI"
            "GEORGE, POPEL" -> "POPEL, GEORGE"
            "J, KLINCEWICZ" -> "KLINCEWICZ, JOHN"
            "JOE, NAUS", "NAUS, JOSEPH" -> "NAUS, JOE"
            "LMIELINSKI, T" -> "IMIELINSKI, TOMASZ" // 01:198
            "LUO, TIANHOA" -> "LUO, TIANHAO"
            "LYNN, AGRE" -> "AGRE, LYNN"
            "MARDEKAIN, JACK" -> "MARDEKIAN, JACK"
            "MICHAEL, MINIER" -> "MINIERE, MICHAEL"
            "NILSEN, MILLER" -> "MILLER, NILSEN"
            "ROJAS, PATRICK" -> "ROJAS, PATRICIO"
            "S, SRINIVASAN" -> "SRINIVASAN, SURYAKALA"
            "SHEILA, LAWRENC", "S, LAWRENCE" -> "LAWRENCE, SHEILA"
            "SHYAM, MOONDRA" -> "MOONDRA, SHYAM"
            "W, STRAWDERMAN" -> "STRAWDERMAN, WILLIAM"
            "ZHANYUN, ZHAO" -> "ZHAO, ZHANYUN"
            else -> prof
        }

        "01:988" -> when (prof) {
            "ALEXANDER-FLOY, N", "ALEXANDER-FLOY" -> "ALEXANDER-FLOYD, NIKOL" // 01:090
            "BALAKRISHNAN, RADIKA" -> "BALAKRISHNAN, RADHIKA"
            "BENBOW, CANDACE" -> "BENBOW, CANDICE"
            "BENSETTI, BENBADER" -> "BENSETTI-BENBADER, HAYET"
            "BURKE", "BURK, TARA" -> "TARA, BURK"
            "COBBLE, S" -> "COBBLE, DOROTHY"
            "EASLEY-HOUSER, M" -> "EASLEY-HOUSER, ARIKA"
            "FLITTERMANLEWIS, SANDY" -> "FLITTERMAN-LEWIS, SANDY" // 01:354
            "GRIFFEN" -> "GRIEFEN, KAT"
            "HAGHANI, FAKHRI", "HAGHANI, FAKARI" -> "HAGHANI, FAKHROLMOLOUK"
            "HEIDI, HOECHST" -> "HOECHST, HEIDI"
            "HETFIELD, LISA", "HETFIELD, LIST" -> "HETFIELD, ELIZABETH"
            "JULIA, WARTENBERG", "WARTENBURG, JULIA", "WARTENBURG, J" -> "WARTENBERG, JULIA"
            "RAJAN, JULIS" -> "RAJAN, JULIE"
            "SANON, JULES" -> "SANON-JULES, LISA"
            "RUSSELL, JONES", "RUSSELL, SANDRA", "RUSSELL-JONES" -> "RUSSEL-JONES, SANDRA" // other places too
            "SCIBELLI, STINA" -> "SODERLING, STINA"
            "SHAHID, SHALEENA" -> "SHAHID, SHAHEENA"
            else -> prof
        }

        "01:991" -> when (prof) {
            "SANCHEZ-INOFUENTES, CELSO" -> "SANCHEZ, CELSO"
            else -> prof
        }

        else -> prof
    }
}
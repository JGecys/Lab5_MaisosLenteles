/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Lab5Gecys;

import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.ListResourceBundle;

/**
 * Platesnėms studijoms:
 * http://download.oracle.com/javase/8/docs/api/java/util/ListResourceBundle.html
 * http://download.oracle.com/javase/tutorial/i18n/resbundle/concept.html
 * Naudojama nustatyta lokalė.
 *
 * @author darius
 */
public class Resources extends ListResourceBundle {

    /**
     * Grąžinamas programos resursų masyvas. Kiti metodai paveldimi iš
     * abstrakčios ListResourceBundle klasės.
     *
     * @return Grąžinamas programos resursų masyvas
     */
    @Override
    public Object[][] getContents() {
        return contents;
    }

    /**
     * Objektų masyvų masyvas, kuriame saugomi programos resursai
     */
    private static final Object[][] contents = {
            {"lblTitle", "KTU IF " + LocalDate.now().getYear() + ". LD5. Maišos lentelės duomenų struktūrų tiriamasis darbas"},
            {"lblAuthor", "<html><b>Autorius: Vardenis Pavardenis, IF-x/x</b><br>email: "
                    + "<FONT COLOR=BLUE>vardenis.pavardenis@ktu.edu</FONT></html>\n"
                    + "Įrašykite savo rekvizitus."},
            {"lblMenus", new String[]{
                    "Failas",
                    "Pagalba"
            }},
            {"lblMenuItems", new String[][]{
                    {"Atidaryti..", "Išsaugoti..", "Išeiti"},
                    {"Apie.."}
            }},
            {"lblNames", new String[]{
                    "Kolizijų sprendimo metodas",
                    "Maišos funkcija",
                    "Duomenų įvedimas"
            }},
            {"lblBorders", new String[]{
                    "Programos vykdymas",
                    "Atvaizdis maišos lentelėje",
                    "Statiniai ir dinaminiai parametrai",
                    "Programos įvykiai"
            }},
            {"cmbCollisionTypes", new String[]{
                    "Atskiros grandinėlės",
                    "Atv. adresacija. Tiesinis dėstymas",
                    "Atv. adresacija. Kvadratinis dėstymas",
                    "Atv. adresacija. Dviguba maiša"
            }},
            {"cmbHashFunctions", new String[]{
                    "Dalyba",
                    "Daugyba",
                    "Iš Java7 Collections Framework",
                    "Iš Java8 Collections Framework"
            }},
            {"btnLabels", new String[]{
                    "Generuoti atvaizdį",
                    "Papildyti atvaizdį iš aibės",
                    "Greitaveikos tyrimas",
                    "Antras Greitaveikos tyrimas"
            }},
            {"lblParams1", new String[]{
                    "Sugeneruotos aibės imtis",
                    "Sugeneruotos aibės dydis",
                    "Celės teksto kirtiklis",
                    "Celės plotis",
                    "Pradinis maišos lentelės dydis",
                    "Apkrovimo faktorius"
            }},
            {"tfParams1", new String[]{
                    "10",
                    "1000",
                    ":",
                    "180",
                    "8",
                    "0.75"
            }},
            {"errMsgs1", new String[]{
                    "Netinkama sugeneruotos aibės imtis",
                    "Netinkamas sugeneruotos aibės dydis",
                    "",
                    "Netinkamas celės plotis",
                    "Netinkamas pradinis maišos lentelės dydis",
                    "Netinkamas apkrovimo faktorius (0;1]"
            }},
            {"lblParams2", new String[]{
                    "Porų kiekis maišos lentelėje",
                    "Maišos lentelės dydis",
                    "Ilgiausia grandinėlė",
                    "Permaišymų kiekis",
                    "Paskutinio papildyto indeksas",
                    "Maišos lentelės užpildymas",
                    "Lentelės eilučių vidurkis",
                    "Tuščios eilutės"
            }},
            {"tfParams2", new String[]{
                    "0",
                    "0",
                    "0",
                    "0",
                    "0",
                    "0",
                    "0",
                    "0"
            }},
            {"lblParams3", new String[]{
                    "Pašalinimo raktas",
                    "Reikšmė",
                    "Jūsų parametras",
                    "Jūsų parametras",
                    "Jūsų parametras",
                    "Jūsų parametras"
            }},
            {"btnNames3", new String[]{
                    "Pašalinti",
                    "Ar turi reikšmę?",
                    "Jūsų parametras",
                    "Jūsų parametras",
                    "Jūsų parametras",
                    "Jūsų parametras"
            }},
            {"tfParams3", new String[]{""}},
            {"errMsgs3", new String[]{""}},
            {"msgs", new String[]{
                    "Dar neįdiegta.",
                    "Atvaizdis papildytas pora iš sugeneruotos aibės",
                    "Greitaveikos tyrimas",
                    "Aibės imtis negali būti didesnė negu\nsugeneruotos aibės dydis.",
                    "Visa sugeneruota aibė patalpinta maišos lentelėje.",
                    "Visa aibė jau išspausdinta",
                    "Failas perskaitytas.",
                    "Maksimalus elementų skaičius grandinėlėje",
                    "Sisteminė klaida. Žiūrėti konsolėje",
                    "Failas neperskaitytas arba yra tuščias"
            }},
            {"toolTips", new String[]{
                    "Galima nustatyti skirtukus: dvitaškį(:), tarpą( ) arba lygybę(=)",
                    "Apkrovimo faktorių nustatykite (0;1] ribose."
            }},
            {"delimiters", ": ="},
            {"keys", new int[][]{
                    {KeyEvent.VK_O, KeyEvent.VK_S, KeyEvent.VK_X},
                    {-1}
            }}
    };
}

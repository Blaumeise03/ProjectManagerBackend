package de.blaumeise03.projectmanager.data.projects.contract;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class InvestmentContractServiceTest {

    @Test
    void preprocessIngameList() {
        String list =
                "ID    Names    Quantity    Valuation \n" +
                "1\tTritanium\t257553179\t515106358.0 \n" +
                "2    Pyerite    62512241    292557287.88 \n" +
                "3    Mexallon    21229933    447314688.31 \n" +
                "4    Isogen    3868504    92341190.48 \n" +
                "5    Nocxium    991804    227400821.12 \n" +
                "6    Zydrine    313587    376527046.77 \n" +
                "7    Megacyte    161427    414307238.31 \n" +
                "8    Morphite    147240    112436881.2 \n" +
                "9    Lustering Alloy    2397671    523675323.11 \n" +
                "10    Sheen Compound    721147    140746259.99 \n" +
                "11    Gleaming Alloy    554777    50401490.45 \n" +
                "12    Condensed Alloy    3191604    576946255.08\n" +
                "13    Precious Alloy    154472    48275589 \n" +
                "14    Glossy Compound    95790    18375395.7 \n" +
                "15    Motley Compound    141034    45960159.92 \n" +
                "16       Opulent Compound  1549006      \t       234782839.42 \n" +
                "17    Fiber Composite    1937464    247278530.32 \n" +
                "18    Lucent Compound    851008    163759469.44 \n" +
                "19    Crystal Compound \t   677301    200521734.06 \n" +
                "20    Dark Compound    481    126806.03 \n" +
                "21    Toxic Metals    322103    275465706.63 \n" +
                "22\t Base Metals    698803    292015797.64 \n" +
                "23    Reactive Metals    477352    202115610.32 \n" +
                "24    Heavy Metals    789860    211461319.2 \n" +
                "25    MK9  Warrior    979202    218254333.78";
        List<String[]> expected = Arrays.asList(new String[][]{
                {"Tritanium", "257553179"},
                {"Pyerite", "62512241"},
                {"Mexallon", "21229933"},
                {"Isogen", "3868504"},
                {"Nocxium", "991804"},
                {"Zydrine", "313587"},
                {"Megacyte", "161427"},
                {"Morphite", "147240"},
                {"Lustering Alloy", "2397671"},
                {"Sheen Compound", "721147"},
                {"Gleaming Alloy", "554777"},
                {"Condensed Alloy", "3191604"},
                {"Precious Alloy", "154472"},
                {"Glossy Compound", "95790"},
                {"Motley Compound", "141034"},
                {"Opulent Compound", "1549006"},
                {"Fiber Composite", "1937464"},
                {"Lucent Compound", "851008"},
                {"Crystal Compound", "677301"},
                {"Dark Compound", "481"},
                {"Toxic Metals", "322103"},
                {"Base Metals", "698803"},
                {"Reactive Metals", "477352"},
                {"Heavy Metals", "789860"},
                {"MK9  Warrior", "979202"}
        });
        List<String[]> result = InvestmentContractService.preprocessIngameList(list);
        //result.forEach(r -> System.out.println("\"" + r[0] + "\", \"" + r[1] + "\""));
        assertAll(
                IntStream
                        .range(0, result.size())
                        .mapToObj(i -> () -> assertArrayEquals(expected.get(i), result.get(i)))
        );
    }
}
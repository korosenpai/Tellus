package Blocks;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import Blocks.Liquids.*;
import Blocks.Solids.DynamicSolid.*;
import Blocks.Solids.StaticSolid.*;;

// functions as big ass lookup table to get new instance of any particle

public class ParticleList {

    public Particle getNewParticle(int id) {
        switch (id) {
            case 0:
                return new Air();
            case 1:
                return new Sand();

            case 2:
                return new Snow();

            case 3:
                return new Wood();

            case 4:
                return new Water();

            case 5:
                return new Gravel();

        }

        System.out.println("GET NEW PARTICLE PARTICLE WAS NOT INDEED FOUND");
        return new Air();
    }

    public Color getColorOfParticle(int id) {
        switch (id) {
           case 1:
               return new Color(182, 155, 99);

           case 2:
               return new Color(205, 205, 205);

           case 3:
               return new Color(83, 54, 43);

           case 4:
               return new Color(46, 82, 255);

           case 5:
               return new Color(83, 84, 78);

        }

        System.out.println("COULD NOT FIND COLOR BOHO");
        return new Color(255, 255, 255);
   }
}

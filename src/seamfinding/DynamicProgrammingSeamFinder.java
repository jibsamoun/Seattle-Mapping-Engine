package seamfinding;

import seamfinding.energy.EnergyFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {

    @Override
    public List<Integer> findHorizontal(Picture picture, EnergyFunction f) {
        int width = picture.width();
        int height = picture.height();
        double[][] energy = new double[width][height];

        // Calculate energy for the first column
        for (int y = 0; y < height; y++) {
            energy[0][y] = f.apply(picture, 0, y);
        }

        // Calculate total energy for the rest of the columns
        for (int x = 1; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double minPrevEnergy = energy[x - 1][y];
                if (y > 0) {
                    minPrevEnergy = Math.min(minPrevEnergy, energy[x - 1][y - 1]);
                }
                if (y < height - 1) {
                    minPrevEnergy = Math.min(minPrevEnergy, energy[x - 1][y + 1]);
                }
                energy[x][y] = f.apply(picture, x, y) + minPrevEnergy;
            }
        }

        // Find the seam
        List<Integer> seam = new ArrayList<>();
        int minIndex = 0;
        for (int y = 1; y < height; y++) {
            if (energy[width - 1][y] < energy[width - 1][minIndex]) {
                minIndex = y;
            }
        }
        seam.add(minIndex);

        for (int x = width - 1; x > 0; x--) {
            int y = seam.get(seam.size() - 1);
            int nextY = y;
            if (y > 0 && energy[x - 1][y - 1] < energy[x - 1][nextY]) {
                nextY = y - 1;
            }
            if (y < height - 1 && energy[x - 1][y + 1] < energy[x - 1][nextY]) {
                nextY = y + 1;
            }
            seam.add(nextY);
        }

        Collections.reverse(seam);
        return seam;
    }
}
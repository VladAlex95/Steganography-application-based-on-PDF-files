package steganografie;

import org.apache.pdfbox.pdmodel.font.PDFont;

public class CarCodificat {
    char car;
    PDFont font;
    float dim;

    public CarCodificat(char car, PDFont font, float dim) {
        this.car = car;
        this.font = font;
        this.dim = dim;
    }
    
}

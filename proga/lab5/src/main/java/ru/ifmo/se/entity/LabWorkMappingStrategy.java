package ru.ifmo.se.entity;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.exceptions.CsvBeanIntrospectionException;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.lang3.builder.Diff;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class LabWorkMappingStrategy extends ColumnPositionMappingStrategy<LabWork> {

    public LabWorkMappingStrategy() {
        this.setType(LabWork.class);
    }

    @Override
    public LabWork populateNewBean(String[] line) throws CsvBeanIntrospectionException, CsvRequiredFieldEmptyException,
            CsvConstraintViolationException, CsvValidationException {
//        Person person = new Person();
//        person.id = line[0];
//        person.lname = line[1];
//        person.fname = line[2];
//        person.address = new Address();
//        person.address.line1 = line[3];
//        person.address.line2 = line[4];
//        return person;
        LabWork labWork = new LabWork();
        labWork.setId(Integer.parseInt(line[0]));
        labWork.setName(line[1]);

        String[] coords = line[2].split("_"); // x_y
        labWork.setCoordinates(new Coordinates(Integer.parseInt(coords[0]), Double.parseDouble(coords[1])));

        DateTimeFormatter formatterLocal = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        labWork.setCreationDate(LocalDate.parse(line[3], formatterLocal));
        labWork.setMinimalPoint(Integer.parseInt(line[4]));
        labWork.setTunedInWorks(Long.parseLong(line[5]));

        if (line[6].equals("EASY")){
            labWork.setDifficulty(Difficulty.EASY);
        }
        if (line[6].equals("NORMAL")){
            labWork.setDifficulty(Difficulty.NORMAL);
        }
        if (line[6].equals("VERY_HARD")){
            labWork.setDifficulty(Difficulty.VERY_HARD);
        }
        if (line[6].equals("VERY_HARD")){
            labWork.setDifficulty(Difficulty.VERY_HARD);
        }
        if (line[6].equals("TERRIBLE")){
            labWork.setDifficulty(Difficulty.TERRIBLE);
        }

        Color color;
        if (line[11].equals("RED")){
            color = Color.RED;
        } else if(line[11].equals("BLACK")){
            color = Color.BLACK;
        } else{
            color = Color.BLUE;
        }

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date;
        try {
            date = format.parse(line[8]);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        labWork.setAuthor(new Person(line[7], date, Integer.parseInt(line[9]), Double.parseDouble(line[10]), color));

        return labWork;
    }

}
package de.swt1.controller;

import de.swt1.database.*;
import de.swt1.vermietung.model.*;

public class GenericController {
    private KundeDAO kundeDAO = new KundeDAO();
    private BuchungDAO buchungDAO = new BuchungDAO();
    private GeschäftskundeDAO geschäftskundeDAO = new GeschäftskundeDAO();
    private ObjektDAO objektDAO = new ObjektDAO();
    private AdresseDAO adresseDAO = new AdresseDAO();
    private ProfilDAO profilDAO = new ProfilDAO();
    private SegmentDAO segmentDAO = new SegmentDAO();

    public GenericController() {}

    public Kunde queryKunde() {
        return new Kunde();
    }

    public void updateKunde() {}

    public Geschätfskunde queryGeschäftskunde() {
        return new Geschätfskunde();
    }

    public void updateGeschäftskunde() {}

    public Buchung queryBuchung() {
        return new Buchung();
    }

    public void updateBuchung() {}

    public Objekt queryObjekt() {
        return new Objekt();
    }

    public void updateObjekt() {}

    public Adresse queryAdresse() {
        return new Adresse();
    }

    public void updateAdresse() {}

    public Profil queryProfil() {
        return new Profil();
    }

    public void updateProfil() {}

    public Segment querySegment() {
        return new Segment();
    }

    public void updateSegment() {}
}

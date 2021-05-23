package com.sevenmap.data.osm.api;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import com.sevenmap.data.osm.Elements.Root;
import com.sevenmap.data.osm.Elements.Bounds.Bounds;
import com.sevenmap.data.osm.parser.extracter;

import org.jdom.input.SAXBuilder;

public class OSMAPI {
  private Root rt;
  private Bounds bd;
  private File mapFile;

  private static org.jdom.Document doc;

  public void downloadMap() {
    downloadMap(false);
  }

  public void downloadMap(Boolean force) {
    boolean exists = mapFile.exists();
    if (force & exists) {
      // Download file from OSM API
      try (BufferedInputStream inputStream = new BufferedInputStream(
          // new
          new URL("https://api.openstreetmap.org/api/0.6/map?bbox=" + bd.getMaxlat() + "," + bd.getMaxlon() + ","
              + bd.getMinlat() + "," + bd.getMinlon()).openStream());
          FileOutputStream fileOS = new FileOutputStream(mapFile)) {
        byte data[] = new byte[1024];
        int byteContent;
        while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
          fileOS.write(data, 0, byteContent);
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void parse() {

    // Open document
    SAXBuilder sxb = new SAXBuilder();
    try {
      doc = sxb.build(mapFile);
    } catch (Exception e) {
      // TODO: handle error
      System.out.println("erreur.");
    }

    // Parse
    try {
      extracter e = new extracter();
      this.rt = e.extract(doc.getRootElement(), Root.class);

    } catch (Exception e) {
      // TODO: handle errors
      e.printStackTrace();
    }

  }

  public OSMAPI() {
  }

  public OSMAPI(Bounds bd, File mapFile) {
    this.bd = bd;
    this.mapFile = mapFile;
  }

  public Root getRt() {
    return this.rt;
  }

  public void setRt(Root rt) {
    this.rt = rt;
  }

  public Bounds getBd() {
    return this.bd;
  }

  public void setBd(Bounds bd) {
    this.bd = bd;
  }

  public File getMapFile() {
    return this.mapFile;
  }

  public void setMapFile(File mapFile) {
    this.mapFile = mapFile;
  }

  public OSMAPI rt(Root rt) {
    setRt(rt);
    return this;
  }

  public OSMAPI bd(Bounds bd) {
    setBd(bd);
    return this;
  }

  public OSMAPI mapFile(File mapFile) {
    setMapFile(mapFile);
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof OSMAPI)) {
      return false;
    }
    OSMAPI oSMAPI = (OSMAPI) o;
    return Objects.equals(rt, oSMAPI.rt) && Objects.equals(bd, oSMAPI.bd) && Objects.equals(mapFile, oSMAPI.mapFile);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rt, bd, mapFile);
  }

  @Override
  public String toString() {
    return "{" + " rt='" + getRt() + "'" + ", bd='" + getBd() + "'" + ", mapFile='" + getMapFile() + "'" + "}";
  }

}

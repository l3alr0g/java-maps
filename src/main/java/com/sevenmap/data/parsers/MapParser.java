package com.sevenmap.data.parsers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.sevenmap.core.Props;
import com.sevenmap.core.Props.BUILD_TYPE;
import com.sevenmap.data.objsept.GeographicCoord;
import com.sevenmap.data.objsept.PlainMap;
import com.sevenmap.data.parsers.geojson.GeoJson;
import com.sevenmap.data.parsers.osm.OSM;
import com.sevenmap.spinel.math.Vector3f;

import org.apache.commons.io.FilenameUtils;

public abstract class MapParser extends Parser {

  protected Props props;
  protected PlainMap generatedMap;

  // <------------------------------- Constructor ------------------------------->
  public MapParser() {
  }

  public MapParser(Props props) {
    this.props = props;
  }
  // <-------------------------------- Code logic ------------------------------->

  /**
   * Download a map from an URL<br>
   * {@code downloadURL} has to be set in props !
   */
  final public void downloadMap() {
    // Download file from OSM API
    String fileName = props.getAppDataPath() + "maps/" + FilenameUtils.getName(props.getDownloadURL().getPath());
    File mapFile = new File(fileName);
    try {
      mapFile.createNewFile();
      FileOutputStream fileOS = new FileOutputStream(mapFile);
      BufferedInputStream inputStream = new BufferedInputStream(props.getDownloadURL().openStream());
      byte data[] = new byte[1024];
      int byteContent;
      while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
        fileOS.write(data, 0, byteContent);
      }
    } catch (IOException ex) {
      // TODO handle exception
    }

  }

  public static void generateURL() throws MalformedURLException {
    // TODO change it to unimplementedMethodException
    // note pour @seba1204 :
    // Je ne peux pas mettre une méthode static et abstraite...
    // Il me la faut absolument static, donc elle ne sera pas
    // abstraite. Du coup il faut espérer que toute les classes
    // qui implémentent cette classe Override cette méthode
    // C'est pourquoi il faut générer une RuntimeError (ou l'autre je sais plus)
    throw new MalformedURLException();
  };

  public abstract void parse();

  public abstract void build();

  /**
   * Store the generated map into the database. <br>
   * Has to be exectuted after parsing and building map !
   */
  final public void store() {

  };

  /**
   * Convert geopgraphic coordinates into regular coords that are dislayable
   * 
   * @param coords geopgraphic coordinates to convert
   * @return Vector3f of coordinates converted
   */
  public static Vector3f GeoCoord2SpinelCoord(GeographicCoord coords) {
    Double x_proj = Math.cos(0.0) * (coords.getLat());
    Double y_proj = coords.getLat();

    Float x = x_proj.floatValue();
    Float y = y_proj.floatValue();
    Float z = 0f;

    return new Vector3f(x, y, z);
  }

  /**
   * Convert regular coords that are dislayable into geopgraphic coordinates
   * 
   * @param vect Vector3f of coordinates
   * @return GeographicCoord of coordinates converted
   */
  public static GeographicCoord SpinelCoord2GeoCoord(Vector3f vect) {
    Double lat = (double) vect.getX();
    Double lon = (double) vect.getY();
    return new GeographicCoord(lat, lon);
  }

  /**
   * {@link Props.hasToBuild} of props has to be {@link BUILD_TYPE.FROM_FILE} or
   * {@link BUILD_TYPE.FROM_URL}, and {@link Props.MapFile} has to be a valid
   * path, or {@link Props.downloadURL} has to be a valid URL
   * 
   * @param props current state of the App
   * @return an ExternalMap with the right type
   */
  public static final MapParser GenerateRightMapType(Props props) {
    String extension = null;
    if (props.hasToBuild().equals(BUILD_TYPE.FROM_FILE)) {
      // get extension of file
      extension = FilenameUtils.getExtension(props.getMapFile());

    } else if (props.hasToBuild().equals(BUILD_TYPE.FROM_URL)) {
      try {

        URL obj = props.getDownloadURL();
        URLConnection conn = obj.openConnection();
        Map<String, List<String>> map = conn.getHeaderFields();

        List<String> contentDisposition = map.get("Content-Disposition");
        if (contentDisposition == null) {
          // TODO: throw error : it is not a downloadable file;
        } else {
          for (String header : contentDisposition) {
            if (header.startsWith("filename=")) {
              extension = FilenameUtils.getExtension(header);
            }
          }
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    if (extension == null) {
      // TODO handle error, URL or file not valid
      System.out.println("ERREUR URL or file not valid");
    }

    switch (extension) {
      case "osm":
        return new OSM(props);
      case "geojson":
      case "json":
        return new GeoJson(props);
      default:
        break;
    }

    return null;
  }

  // <--------------------------- Getter and setters ---------------------------->

  public Props getProps() {
    return this.props;
  }

  public void setProps(Props props) {
    this.props = props;
  }

  public MapParser props(Props props) {
    setProps(props);
    return this;
  }

  // <---------------------------- Object overrides ----------------------------->

  @Override
  public String toString() {
    return "{" + " props='" + getProps() + "'" + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (!(o instanceof MapParser)) {
      return false;
    }
    MapParser externalMap = (MapParser) o;
    return Objects.equals(props, externalMap.props);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(props);
  }

}

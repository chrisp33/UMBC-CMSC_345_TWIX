using System.Runtime.InteropServices; // necessary for the Guid and ProgId attributes

namespace Org.Jawin.NETTest {

	[Guid("25c2f5a2-1afe-36ce-BE27-84E040F5E19A")]
	[ProgId("Jawin.HelloFromNET")] // Register the CLSID under this shortcut ProgId
	public class HelloFromNET : HelloFromNETItf {
	
		// Need a public default no-arg constructor for COM Interop.
		public HelloFromNET() {
		}

		public string Caption {
			set { this.caption = value; }
		}
	
		public void ShowDialog(string str) {
			System.Windows.Forms.MessageBox.Show(str, caption);
		}
	
		private string caption;
	}
}

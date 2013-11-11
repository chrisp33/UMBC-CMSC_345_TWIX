using System.Runtime.InteropServices; // necessary for the Guid attribute

namespace Org.Jawin.NETTest {

	[Guid("3823a63d-5891-3b4f-A460-DB0FB847075A")]
	public interface HelloFromNETItf {
		
		// a writeable property named Caption
		string Caption {
			set;
		}

		void ShowDialog(string str);
	}
}
